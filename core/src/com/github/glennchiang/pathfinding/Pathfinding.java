package com.github.glennchiang.pathfinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.glennchiang.pathfinding.algorithms.*;
import com.github.glennchiang.pathfinding.visualization.AlgorithmVisualizer;
import com.github.glennchiang.pathfinding.visualization.VisualGrid;

public class Pathfinding extends ApplicationAdapter {
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 800;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private Stage stage;

    private Grid grid;
    private VisualGrid visualGrid;
    private AlgorithmVisualizer visualizer;

    @Override
    public void create() {
        // Boilerplate setup
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();

        // Set up stage
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Initialize grid for pathfinder to act on
        grid = new Grid(20, 32);
        grid.setStart(0, 0);
//        grid.setTarget(5, 8);
        grid.setTarget(grid.numRows - 1, grid.numCols - 1);
        grid.setObstacles();

        // Visual representation of grid and algorithm
        int gridWidth = 640;
        int gridHeight = 400;
        visualGrid = new VisualGrid((SCREEN_WIDTH - gridWidth) / 2, (SCREEN_HEIGHT - gridHeight) / 2,
                gridWidth, gridHeight, grid, shapeRenderer);

        visualizer = new AlgorithmVisualizer(visualGrid);

        Pathfinder aStar = new AStarAlgorithm();
        Pathfinder greedy = new GreedyAlgorithm();
        Pathfinder dijkstra = new DijkstraAlgorithm();
        Pathfinder[] algorithms = new Pathfinder[]{ aStar, greedy, dijkstra };

//        for (Pathfinder algorithm: algorithms) {
//            AlgorithmSolution solution = algorithm.findPath(grid);
//            visualizer.visualize(solution);
//        }
        AlgorithmSolution solution = aStar.findPath(grid);
        visualizer.visualize(solution);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

		visualGrid.renderGrid();
        visualizer.update();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
    }
}
