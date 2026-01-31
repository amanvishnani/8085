package com.amanvishnani.sim8085;

import com.amanvishnani.sim8085.domain.*;
import com.amanvishnani.sim8085.domain.Impl.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private final I8085 simulator = new Simulator();
    private List<InstructionRow> rows = new ArrayList<>();

    // UI Components
    private TextArea codeEditor;
    private TableView<Object[]> codeTable;
    private TableView<Object[]> dataTable;
    private TableView<Object[]> stackTable;
    private TextField codeHead, dataHead, stackHead;
    private Label labelA, labelB, labelC, labelD, labelE, labelH, labelL;
    private Label labelIP, labelSP;
    private Label flagS, flagZ, flagAc, flagP, flagCy;
    private boolean isDarkMode = true;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        simulator.initialize();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        // Top Toolbar
        HBox toolbar = createToolbar();
        root.setTop(toolbar);

        // Center SplitPane
        SplitPane centerSplit = new SplitPane();
        centerSplit.getStyleClass().add("main-split-pane");

        // Code Editor Section
        VBox editorBox = createEditorSection();
        editorBox.getStyleClass().add("editor-box");

        // Tables Section
        TabPane tabPane = createTablesSection();
        tabPane.getStyleClass().add("tables-box");

        centerSplit.getItems().addAll(editorBox, tabPane);
        centerSplit.setDividerPositions(0.4);
        root.setCenter(centerSplit);

        VBox sidebar = createSidebar();
        root.setRight(sidebar);

        scene = new Scene(root, 1200, 800);
        applyTheme();

        primaryStage.setTitle("Sim8085 - Modern JavaFX Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        updateView();
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(15));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");

        Button loadBtn = new Button("LOAD >");
        loadBtn.getStyleClass().add("btn-primary");
        loadBtn.setOnAction(e -> loadInstructions());

        Button runBtn = new Button("RUN");
        runBtn.getStyleClass().add("btn-primary");
        runBtn.setId("runBtn");
        runBtn.setDisable(true);
        runBtn.setOnAction(e -> runProgram());

        Button stepBtn = new Button("1 STEP >");
        stepBtn.getStyleClass().add("btn-secondary");
        stepBtn.setId("stepBtn");
        stepBtn.setDisable(true);
        stepBtn.setOnAction(e -> {
            simulator.execute(simulator.getDataAtIP().hexValue());
        });

        Button resetBtn = new Button("RESET");
        resetBtn.getStyleClass().add("btn-danger");
        resetBtn.setOnAction(e -> {
            simulator.initialize();
            scene.lookup("#runBtn").setDisable(false);
            scene.lookup("#stepBtn").setDisable(false);
            updateView();
        });

        Label title = new Label("8085 SIMULATOR");
        title.getStyleClass().add("app-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(title, spacer);

        Button themeBtn = new Button("🌓");
        themeBtn.getStyleClass().add("btn-theme");
        themeBtn.getStyleClass().add("btn-secondary"); // Reuse consistent padding
        themeBtn.setOnAction(e -> {
            isDarkMode = !isDarkMode;
            applyTheme();
        });

        toolbar.getChildren().addAll(themeBtn, loadBtn, runBtn, stepBtn, resetBtn);
        return toolbar;
    }

    private void applyTheme() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/base.css").toExternalForm());
        String themeFile = isDarkMode ? "/dark.css" : "/light.css";
        scene.getStylesheets().add(getClass().getResource(themeFile).toExternalForm());
    }

    private void subscribeInstructionPointer() {
        this.simulator.onInstructionExecuted(exec -> {
            if (exec.getInstruction().hexValue().equals("76")) {
                scene.lookup("#stepBtn").setDisable(true);
            }
            updateView();
            highlightCurrentInstruction();
        });

        simulator.onError(err -> alert(err.getMessage()));
    }

    private void highlightCurrentInstruction() {
        int ip = simulator.getIP().intValue();
        int head = 0;
        try {
            head = Integer.parseInt(codeHead.getText().replace("H", "").trim(), 16);
        } catch (Exception ignored) {
        }

        int index = ip - head;
        if (index >= 0 && index < codeTable.getItems().size()) {
            codeTable.getSelectionModel().select(index);
            codeTable.scrollTo(index);
        }
    }

    private void runProgram() {
        scene.lookup("#runBtn").setDisable(true);
        scene.lookup("#stepBtn").setDisable(true);

        int maxInstructions = 1000;
        int count = 0;
        while (count < maxInstructions) {
            String op = simulator.getDataAtIP().hexValue();
            simulator.execute(op);
            count++;
            if (op.equals("76")) { // HLT
                break;
            }
        }

        if (count >= maxInstructions) {
            alert("Program Execution stopped: Safety limit reached (1000 instructions).");
        }
    }

    private VBox createEditorSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        Label label = new Label("Assembly Code");
        label.getStyleClass().add("section-label");

        codeEditor = new TextArea();
        codeEditor.setText(
                "MVI A,0FH\nMVI B,12H\nSUB B\nJM NEXT\nSTA 4000H\nNEXT:CALL SBROUT\nHLT\n\nSBROUT:STA 4005H\nRET");
        VBox.setVgrow(codeEditor, Priority.ALWAYS);
        codeEditor.getStyleClass().add("code-editor");

        box.getChildren().addAll(label, codeEditor);
        return box;
    }

    private TabPane createTablesSection() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("tables-tabpane");

        // Code Tab
        Tab codeTab = new Tab("CODE", createCodeTableView());
        codeTab.setClosable(false);

        // Data Tab
        Tab dataTab = new Tab("DATA", createDataTableView());
        dataTab.setClosable(false);

        // Stack Tab
        Tab stackTab = new Tab("STACK", createStackTableView());
        stackTab.setClosable(false);

        tabPane.getTabs().addAll(codeTab, dataTab, stackTab);
        return tabPane;
    }

    private VBox createCodeTableView() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));

        HBox headBox = new HBox(10);
        headBox.setAlignment(Pos.CENTER_LEFT);
        codeHead = new TextField("0000");
        codeHead.setPrefWidth(80);
        Button setBtn = new Button("Set");
        setBtn.setOnAction(e -> updateCodeTable());
        Label displayLabel = new Label("Display from:");
        displayLabel.getStyleClass().add("section-label");
        Label hLabel = new Label("H");
        hLabel.getStyleClass().add("section-label");
        headBox.getChildren().addAll(displayLabel, codeHead, hLabel, setBtn);

        codeTable = new TableView<>();
        setupTableColumns(codeTable, "Memory", "Label", "Instruction", "HEX");
        VBox.setVgrow(codeTable, Priority.ALWAYS);

        vBox.getChildren().addAll(headBox, codeTable);
        return vBox;
    }

    private VBox createDataTableView() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));

        HBox headBox = new HBox(10);
        headBox.setAlignment(Pos.CENTER_LEFT);
        dataHead = new TextField("4000");
        dataHead.setPrefWidth(80);
        Button setBtn = new Button("Set");
        setBtn.setOnAction(e -> updateDataTable());
        Label displayLabel = new Label("Display from:");
        displayLabel.getStyleClass().add("section-label");
        Label hLabel = new Label("H");
        hLabel.getStyleClass().add("section-label");
        headBox.getChildren().addAll(displayLabel, dataHead, hLabel, setBtn);

        dataTable = new TableView<>();
        setupTableColumns(dataTable, "Memory", "Value (HEX)");
        VBox.setVgrow(dataTable, Priority.ALWAYS);

        vBox.getChildren().addAll(headBox, dataTable);
        return vBox;
    }

    private VBox createStackTableView() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));

        HBox headBox = new HBox(10);
        headBox.setAlignment(Pos.CENTER_LEFT);
        stackHead = new TextField("FFFF");
        stackHead.setPrefWidth(80);
        Button setBtn = new Button("Set");
        setBtn.setOnAction(e -> updateStackTable());
        Label displayLabel = new Label("Display from:");
        displayLabel.getStyleClass().add("section-label");
        Label hLabel = new Label("H");
        hLabel.getStyleClass().add("section-label");
        headBox.getChildren().addAll(displayLabel, stackHead, hLabel, setBtn);

        stackTable = new TableView<>();
        setupTableColumns(stackTable, "Memory", "Value (HEX)");
        VBox.setVgrow(stackTable, Priority.ALWAYS);

        vBox.getChildren().addAll(headBox, stackTable);
        return vBox;
    }

    private void setupTableColumns(TableView<Object[]> table, String... headers) {
        for (int i = 0; i < headers.length; i++) {
            final int index = i;
            TableColumn<Object[], String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(
                    data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue()[index])));
            table.getColumns().add(col);
        }
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.getStyleClass().add("sidebar");

        Label regTitle = new Label("REGISTERS");
        regTitle.getStyleClass().add("sidebar-title");

        GridPane regGrid = new GridPane();
        regGrid.setHgap(10);
        regGrid.setVgap(10);

        labelA = addRegRow(regGrid, "ACC (A)", 0);
        labelB = addRegRow(regGrid, "REG (B)", 1);
        labelC = addRegRow(regGrid, "REG (C)", 2);
        labelD = addRegRow(regGrid, "REG (D)", 3);
        labelE = addRegRow(regGrid, "REG (E)", 4);
        labelH = addRegRow(regGrid, "REG (H)", 5);
        labelL = addRegRow(regGrid, "REG (L)", 6);

        Label ptrTitle = new Label("POINTERS");
        ptrTitle.getStyleClass().add("sidebar-title");
        GridPane ptrGrid = new GridPane();
        ptrGrid.setHgap(10);
        ptrGrid.setVgap(10);
        labelIP = addRegRow(ptrGrid, "INST. PTR", 0);
        labelSP = addRegRow(ptrGrid, "STACK PTR", 1);

        Label flagTitle = new Label("FLAGS");
        flagTitle.getStyleClass().add("sidebar-title");
        HBox flagBox = new HBox(10);
        flagBox.setAlignment(Pos.CENTER);
        flagS = createFlagCircle("S");
        flagZ = createFlagCircle("Z");
        flagAc = createFlagCircle("Ac");
        flagP = createFlagCircle("P");
        flagCy = createFlagCircle("Cy");
        flagBox.getChildren().addAll(flagS, flagZ, flagAc, flagP, flagCy);

        sidebar.getChildren().addAll(regTitle, regGrid, ptrTitle, ptrGrid, flagTitle, flagBox);
        return sidebar;
    }

    private Label addRegRow(GridPane grid, String name, int row) {
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("sidebar-label");
        grid.add(nameLabel, 0, row);
        Label val = new Label("00");
        val.getStyleClass().add("reg-value");
        grid.add(val, 1, row);
        return val;
    }

    private Label createFlagCircle(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("flag-label");
        return l;
    }

    private void updateView() {
        // Registers
        Map<String, String> regs = MainHelper.getRegistersMap(simulator);
        labelA.setText(regs.get("A"));
        labelB.setText(regs.get("B"));
        labelC.setText(regs.get("C"));
        labelD.setText(regs.get("D"));
        labelE.setText(regs.get("E"));
        labelH.setText(regs.get("H"));
        labelL.setText(regs.get("L"));

        // Pointers
        labelIP.setText(simulator.getIP().hexValue());
        labelSP.setText(simulator.getSP().hexValue());

        // Flags
        Map<String, String> flags = MainHelper.getFlagsMap(simulator);
        updateFlagUI(flagS, flags.get("S"));
        updateFlagUI(flagZ, flags.get("Z"));
        updateFlagUI(flagAc, flags.get("Ac"));
        updateFlagUI(flagP, flags.get("P"));
        updateFlagUI(flagCy, flags.get("Cy"));

        updateCodeTable();
        updateDataTable();
        updateStackTable();
    }

    private void updateFlagUI(Label label, String val) {
        if ("1".equals(val) || "true".equals(val)) {
            label.getStyleClass().add("flag-on");
        } else {
            label.getStyleClass().remove("flag-on");
        }
    }

    private void updateCodeTable() {
        int head = MainHelper.parseHex(codeHead.getText(), 0, 0, 0x3FFF, "Code Segment Head", msg -> alert(msg));
        if (head != -1) {
            codeTable.getItems().clear();
            codeTable.getItems().addAll(MainHelper.getCodeTableData(rows, head));
        }
    }

    private void updateDataTable() {
        int head = MainHelper.parseHex(dataHead.getText(), 0x4000, 0x4000, 0xDFFF, "Data Segment Head",
                msg -> alert(msg));
        if (head != -1) {
            dataTable.getItems().clear();
            dataTable.getItems().addAll(MainHelper.getMemoryTableData(simulator, head, 0xDFFF));
        }
    }

    private void updateStackTable() {
        int head = MainHelper.parseHex(stackHead.getText(), 0xFFFF, 0xE000, 0xFFFF, "Stack Segment Head",
                msg -> alert(msg));
        if (head != -1) {
            stackTable.getItems().clear();
            stackTable.getItems().addAll(MainHelper.getMemoryTableData(simulator, head, 0xFFFF));
        }
    }

    private void loadInstructions() {
        simulator.initialize();
        subscribeInstructionPointer();
        String code = codeEditor.getText().toUpperCase();
        this.rows = simulator.compile(code);
        scene.lookup("#runBtn").setDisable(false);
        scene.lookup("#stepBtn").setDisable(false);
        updateView();
        highlightCurrentInstruction();
    }

    private void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
