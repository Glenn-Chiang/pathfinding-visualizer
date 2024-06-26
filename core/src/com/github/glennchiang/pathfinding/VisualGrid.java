package com.github.glennchiang.pathfinding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.glennchiang.pathfinding.algorithms.AlgorithmStep;
import com.github.glennchiang.pathfinding.algorithms.Node;

import java.awt.*;

public class VisualGrid {
    private final ShapeRenderer renderer;
    private final Grid grid;
    private final Rectangle[][] cells;

    public VisualGrid(int x, int y, int width, int height, Grid grid, ShapeRenderer renderer) {
        this.grid = grid;
        this.renderer = renderer;

        cells = new Rectangle[grid.numRows][grid.numCols];
        int cellWidth = width / grid.numCols;
        int cellHeight = height / grid.numRows;

        for (int row = 0; row < grid.numRows; row++) {
            for (int col = 0; col < grid.numCols; col++) {
                Rectangle cell = new Rectangle();
                cell.x = x + col * cellWidth;
                cell.y = y + height - (row + 1) * cellHeight;
                cell.width = cellWidth;
                cell.height = cellHeight;
                cells[row][col] = cell;
            }
        }
    }

    // Draw the grid, marking out obstacles, start cell and target cell
    // This method is not affected in any way by the algorithm that is being visualized on the grid
    // It is only interested in rendering the static obstacle positions and start/target positions
    public void renderGrid() {
        // Draw cells
        for (int row = 0; row < grid.numRows; row++) {
            for (int col = 0; col < grid.numCols; col++) {
                Rectangle cell = cells[row][col];

                // Draw cell border
                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.valueOf("#2980B9"));
                renderer.rect(cell.x, cell.y, cell.width, cell.height);
                renderer.end();

                CellType cellType = grid.getCell(row, col);
                Color color;
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                switch (cellType) {
                    case START:
                        color = Color.valueOf("#58D68D");
                        break;
                    case TARGET:
                        color = Color.valueOf("#E74C3C");
                        break;
                    case OBSTACLE:
                        color = Color.valueOf("#21618C");
                        break;
                    case EMPTY:
                    default:
                        color = Color.WHITE;
                        break;
                }
                renderer.setColor(color);
                renderer.rect(cell.x, cell.y, cell.width - 1, cell.height - 1);
                renderer.end();
            }
        }
    }

    // Renders the given algorithm step by marking out the current path as well as open and closed nodes
    // Should only be called by AlgorithmVisualizer
    public void renderStep(AlgorithmStep algorithmStep) {
        // Mark open nodes
        for (Node node: algorithmStep.openNodes) {
            markNode(node, Color.SKY);
        }

        // Mark closed nodes
        for (Node node: algorithmStep.closedNodes) {
            markNode(node, Color.LIGHT_GRAY);
        }

        // Mark current path
        for (Node node: algorithmStep.currentPath) {
            markNode(node, Color.GREEN);
        }
    }

    private void markNode(Node node, Color color) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        Rectangle cell = cells[node.getRow()][node.getCol()];
        renderer.rect(cell.x, cell.y, cell.width - 1, cell.height - 1);
        renderer.end();
    }
}
