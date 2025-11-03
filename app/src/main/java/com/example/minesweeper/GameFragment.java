package com.example.minesweeper;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class GameFragment extends Fragment {
    private Minefield minefield;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        int rows = args.getInt("rows");
        int cols = args.getInt("cols");
        int minePercent = args.getInt("minePercent");

        minefield = new Minefield(rows, cols, minePercent);

        GridLayout gridLayout = view.findViewById(R.id.gameGrid);
        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(cols);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int padding = 16; // total padding per button
        int buttonSize = (screenWidth / cols) - padding;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Button cellButton = new Button(requireContext());
                int finalR = r, finalC = c;

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(4, 4, 4, 4);
                cellButton.setLayoutParams(params);

                cellButton.setOnClickListener(v -> handleClick(finalR, finalC, cellButton));
                gridLayout.addView(cellButton);
            }
        }

    }

    private void handleClick(int r, int c, Button button) {
        Cell cell = minefield.getCell(r, c);
        if (cell.isRevealed) return;

        cell.isRevealed = true;
        button.setClickable(false);
        button.setFocusable(false);
        button.setTextSize(18);

        if (cell.isMine) {
            button.setText("💣");
            button.setBackgroundColor(Color.parseColor("#FF5252"));
            Toast.makeText(requireContext(), "You Lost! 💥", Toast.LENGTH_SHORT).show();
            disableAllButtons((ViewGroup) getView());
        } else {
            button.setText(cell.adjacentMines == 0 ? "" : String.valueOf(cell.adjacentMines));
            button.setBackgroundColor(Color.parseColor("#E0E0E0"));
            button.setTextColor(getColorForNumber(cell.adjacentMines));
        }
    }

    private void disableAllButtons(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Button) child.setEnabled(false);
            else if (child instanceof ViewGroup) disableAllButtons((ViewGroup) child);
        }
    }

    private int getColorForNumber(int num) {
        switch (num) {
            case 1: return Color.parseColor("#1976D2");
            case 2: return Color.parseColor("#388E3C");
            case 3: return Color.parseColor("#D32F2F");
            case 4: return Color.parseColor("#7B1FA2");
            case 5: return Color.parseColor("#F57C00");
            case 6: return Color.parseColor("#0097A7");
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.DKGRAY;
        }
    }
}

class Cell {
    boolean isMine = false;
    boolean isRevealed = false;
    boolean isFlagged = false;
    int adjacentMines = 0;
}

class Minefield {
    private final int rows;
    private final int cols;
    private final int numMines;
    private final Cell[][] grid;

    Minefield(int rows, int cols, int minePercent) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = (rows * cols * minePercent) / 100;
        grid = new Cell[rows][cols];
        initGrid();
        placeMines();
        countAdjacents();
    }

    private void initGrid() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell();
    }

    private void placeMines() {
        java.util.Random rand = new java.util.Random();
        int placed = 0;
        while (placed < numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!grid[r][c].isMine) {
                grid[r][c].isMine = true;
                placed++;
            }
        }
    }

    private void countAdjacents() {
        int[] dirs = {-1, 0, 1};
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].isMine) continue;
                int count = 0;
                for (int dr : dirs)
                    for (int dc : dirs)
                        if (!(dr == 0 && dc == 0)) {
                            int nr = r + dr, nc = c + dc;
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc].isMine)
                                count++;
                        }
                grid[r][c].adjacentMines = count;
            }
        }
    }

    Cell getCell(int r, int c){
        return grid[r][c];
    }
}
