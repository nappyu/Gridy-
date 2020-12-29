package ast;

import Gridy.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public class Evaluator implements Visitor<Object> {

    public Evaluator() {

    }
    // We might want table for each object type. For now I just put Object cuz I'm lazy.
    private  Gridy game;
    private static final Map<String, Cell> cellTable = new HashMap<>();
    private static final Map<String, Repeat> repeatTable = new HashMap<>();
    private static final Map<String, Function> functionTable = new HashMap<>();
    private static final Map<String, Section> sectionTable = new HashMap<>();

    public static Map<String, Cell> getCellTable() {
        return cellTable;
    }

    public static Map<String, Repeat> getRepeatTable() {
        return repeatTable;
    }

    public Gridy getGame() {
        return game;
    }

    public static Map<String, Function> getFunctionTable() {
        return functionTable;
    }

    public static Map<String, Section> getSectionTable() {
        return sectionTable;
    }

    @Override
    public Object visit(Program p) {
        for (Statement s : p.getStatements()) {
            s.accept(this);
        }
        return null;
    }

    /**
     * initialize and create the game
     * @param gCreate
     * @return
     */
    @Override
    public Object visit(GCreate gCreate) {
        int x = Integer.parseInt(gCreate.getX());
        int y = Integer.parseInt(gCreate.getY());
        int t =  Integer.parseInt(gCreate.getDuration());
        String fileName = gCreate.getFileName();
        if (fileName == null)
            game = new Gridy(x, y, t);
        else {
            if (new File(System.getProperty("user.dir") + "\\bgm\\"+fileName).exists())
                game = new Gridy(x, y, t, fileName);
            else
                throw new Error("Such music file does not exist in the bgm folder.");
        }

        return null;
    }

    /**
     * add a section to the game
     * @param section
     * @return
     */
    @Override
    public Object visit(AddSection section) {
        String startTime = section.getStartTime();
        if (startTime != null) {
            game.addSection(sectionTable.get(section.getName()), Integer.parseInt(startTime));
        } else {
            game.addSection(sectionTable.get(section.getName()));
        }
        return null;
    }


    /**
     * declare and create a cell
     * @param dCell
     * @return
     */
    @Override
    public Object visit(DecCell dCell) {
        try {
            Color c = Color.valueOf(dCell.getColor());
            Cell cell = new Cell(Double.parseDouble(dCell.getDuration()), c, Integer.parseInt(dCell.getScore()));
            cellTable.put(dCell.getName(), cell);
        } catch (Error e) {
            throw new Error("Invalid Colour Format.");
        }
        return null;
    }

    /**
     * declare and create a repeat
     * @param dRepeat
     * @return
     */
    @Override
    public Object visit(DecRepeat dRepeat) {
        Cell cell = cellTable.get(dRepeat.getCellName());
        if (cell == null)
            throw new Error("Such Cell does not exist.");
        Repeat repeat = new Repeat(cell, Integer.parseInt(dRepeat.getFrequency()), Integer.parseInt(dRepeat.getDuration()));
        repeatTable.put(dRepeat.getName(), repeat);
        return null;
    }


    /**
     * declare and create a function
     * @param dFunction
     * @return
     */
    @Override
    public Object visit(DecFunction dFunction) {
        Cell cell = cellTable.get(dFunction.getCellName());
        if (cell == null)
            throw new Error("Such Cell does not exist.");
        Function function = new Function(dFunction.getxEquation(), dFunction.getyEquation(), cell, dFunction.getDuration());
        functionTable.put(dFunction.getName(), function);
        return null;
    }


    /**
     * declare and create a section
     * @param dSection
     * @return
     */
    @Override
    public Object visit(DecSection dSection) {
        Section section = new Section(Integer.parseInt(dSection.getDuration()), game.getX(), game.getY());
        sectionTable.put(dSection.getName(), section);
        return null;
    }


    /**
     * add a cell or a repeat to a section
     * @param addSimp
     * @return
     */
    @Override
    public Object visit(AddSimp addSimp) {
        Section section = sectionTable.get(addSimp.getSectionName());
        if (section != null) {
            int x =  Integer.parseInt(addSimp.getX());
            int y = Integer.parseInt(addSimp.getY());
            int startTime = Integer.parseInt(addSimp.getStartTime());

            if (cellTable.get(addSimp.getName()) == null && repeatTable.get(addSimp.getName()) == null)
                throw new Error("No such Cell or Repeat has been declared previously.");

            if (cellTable.get(addSimp.getName()) != null)
                section.add(cellTable.get(addSimp.getName()), x, y, startTime);
            else if (repeatTable.get(addSimp.getName()) != null)
                section.add(repeatTable.get(addSimp.getName()), x, y, startTime);

            sectionTable.put(addSimp.getSectionName(), section);
        } else {
            throw new Error("No such Section has been declared previously.");
        }
        return null;
    }

    /**
     * add a function to a section
     * @param addFunc
     * @return
     */
    @Override
    public Object visit(AddFunc addFunc) {
        Section section = sectionTable.get(addFunc.getSectionName());
        section.add(functionTable.get(addFunc.getName()), Integer.parseInt(addFunc.getStartTime()));
        sectionTable.put(addFunc.getSectionName(), section);
        return null;
    }

    /**
     * adjust the difficulty of a section with a multiplier
     * @param difficulty
     * @return
     */
    @Override
    public Object visit(Difficulty difficulty) {
        game.setDifficulty(Integer.parseInt(difficulty.getLevel()));
        return null;
    }

}
