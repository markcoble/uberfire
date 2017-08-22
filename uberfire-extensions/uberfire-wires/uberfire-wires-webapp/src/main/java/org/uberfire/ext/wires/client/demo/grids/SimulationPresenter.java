/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.ext.wires.client.demo.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.ext.wires.client.resources.i18n.WiresGridsDemoConstants;
import org.uberfire.ext.wires.core.grids.client.model.Bounds;
import org.uberfire.ext.wires.core.grids.client.model.GridColumn;
import org.uberfire.ext.wires.core.grids.client.model.GridData;
import org.uberfire.ext.wires.core.grids.client.model.GridRow;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseBounds;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridData;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.model.impl.BaseHeaderMetaData;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.impl.CheckBoxDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.impl.ListBoxSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.impl.TextBoxSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.grid.GridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.columns.BooleanDOMElementColumn;
import org.uberfire.ext.wires.core.grids.client.widget.grid.columns.ListBoxDOMElementSingletonColumn;
import org.uberfire.ext.wires.core.grids.client.widget.grid.columns.RowNumberColumn;
import org.uberfire.ext.wires.core.grids.client.widget.grid.columns.StringDOMElementSingletonColumn;
import org.uberfire.ext.wires.core.grids.client.widget.grid.columns.StringPopupColumn;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.BaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.BaseGridWidgetKeyboardHandler;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationClearCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationEditCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveDown;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveLeft;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveRight;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationMoveUp;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationSelectBottomRightCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.impl.KeyboardOperationSelectTopLeftCell;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.columns.impl.StringColumnRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.grids.impl.BaseGridRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.grids.impl.BaseGridRendererHelper;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.themes.GridRendererTheme;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.themes.impl.MultiColouredTheme;
import org.uberfire.ext.wires.core.grids.client.widget.grid.renderers.themes.impl.RedTheme;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

/**
 * A Workbench Screen to create Scenario Simulations.
 */
@Dependent
@WorkbenchScreen(identifier = "SimulationPresenter")
public class SimulationPresenter implements WiresGridsDemoView.Presenter {

    private static final int GRID1_ROWS = 100;

    private Menus menus;
    private WiresGridsDemoView view;

    private GridWidget gridWidget1;

    private TranslationService translationService;

    @Inject
    public SimulationPresenter(final WiresGridsDemoView view,
                               final TranslationService translationService) {
        this.view = view;
        this.translationService = translationService;
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return translationService.getTranslation(WiresGridsDemoConstants.Screen_Title);
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return view;
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    @PostConstruct
    public void setup() {
        setupMenus();
        setupKeyDownHandler();
        setupZoomChangeHandler();
        setupStyleChangeHandler();
        setupMergedStateValueChangeHandler();
        setupAppendRowClickHandler();
        setupDeleteRowClickHandler();

        this.gridWidget1 = makeGridWidget1();

        view.add(gridWidget1);
    }

    private void linkGrids(final GridWidget sourceGridWidget,
                           final int sourceGridColumnIndex,
                           final GridWidget targetGridWidget,
                           final int targetGridColumnIndex) {
        final GridColumn<?> sourceGridColumn = sourceGridWidget.getModel().getColumns().get(sourceGridColumnIndex);
        final GridColumn<?> targetGridColumn = targetGridWidget.getModel().getColumns().get(targetGridColumnIndex);
        sourceGridColumn.setLink(targetGridColumn);
    }

    private void setupMenus() {
        this.menus = MenuFactory
                                .newTopLevelMenu(translationService.getTranslation(WiresGridsDemoConstants.Menu_ClearSelections))
                                .respondsWith(new Command() {

                                    @Override
                                    public void execute() {
                                        for (GridWidget gridWidget : view.getGridWidgets()) {
                                            if (gridWidget.isSelected()) {
                                                gridWidget.getModel().clearSelections();
                                            }
                                        }
                                        view.refresh();
                                        menus.getItems().get(0).setEnabled(false);
                                        menus.getItems().get(1).setEnabled(false);
                                    }
                                })
                                .endMenu()
                                .newTopLevelMenu(translationService.getTranslation(WiresGridsDemoConstants.Menu_ClearCells))
                                .respondsWith(new Command() {

                                    @Override
                                    public void execute() {
                                        clearCells();
                                    }
                                })
                                .endMenu()
                                .build();
        menus.getItems().get(0).setEnabled(false);
        menus.getItems().get(1).setEnabled(false);
    }

    private void setupKeyDownHandler() {
        final GridLayer layer = view.getGridLayer();
        final BaseGridWidgetKeyboardHandler handler = new BaseGridWidgetKeyboardHandler(view.getGridLayer());
        handler.addOperation(new KeyboardOperationClearCell(layer),
                             new KeyboardOperationEditCell(layer),
                             new KeyboardOperationMoveLeft(layer),
                             new KeyboardOperationMoveRight(layer),
                             new KeyboardOperationMoveUp(layer),
                             new KeyboardOperationMoveDown(layer),
                             new KeyboardOperationSelectTopLeftCell(layer),
                             new KeyboardOperationSelectBottomRightCell(layer));

        view.addKeyDownHandler(handler);
    }

    private GridWidget makeGridWidget1() {
        final GridData grid1 = new BaseGridData(false);
        grid1.setHeaderRowCount(2);
        final GridWidget gridWidget1 = new BaseGridWidget(grid1,
                                                          this,
                                                          view.getGridLayer(),
                                                          new BaseGridRenderer(new MultiColouredTheme()));

        //Add a floating column for row number
        final RowNumberColumn grid1ColumnRowNumber = new RowNumberColumn();
        grid1.appendColumn(grid1ColumnRowNumber);

        //Add a floating column
        final GridColumn.HeaderMetaData grid1ColumnFloatingHeaderMetaData = new BaseHeaderMetaData("Time");
        final TextBoxSingletonDOMElementFactory grid1ColumnFloatingFactory = new TextBoxSingletonDOMElementFactory(view.getGridPanel(),
                                                                                                                   view.getGridLayer(),
                                                                                                                   gridWidget1);
        final BaseGridColumn<String> grid1ColumnFloating = new StringDOMElementSingletonColumn(grid1ColumnFloatingHeaderMetaData,
                                                                                               grid1ColumnFloatingFactory,
                                                                                               100);
        grid1ColumnFloating.setMovable(false);
        grid1ColumnFloating.setResizable(true);
        grid1ColumnFloating.setFloatable(true);
        grid1.appendColumn(grid1ColumnFloating);

        addGridColumnGroup(grid1,
                           "Command",
                           "name",
                           "arg1",
                           "arg2");

        GridDataFactory.populate(grid1,
                                 GRID1_ROWS);

        gridWidget1.setLocation(new Point2D(-1300,
                                            0));

        return gridWidget1;
    }

    private void setupZoomChangeHandler() {
        view.addZoomChangeHandler(new ChangeHandler() {

            private int m_currentZoom = 100;

            @Override
            public void onChange(final ChangeEvent event) {
                final int pct = view.getSelectedZoomLevel();
                if (m_currentZoom == pct) {
                    return;
                }
                m_currentZoom = pct;
                view.setZoom(pct);
            }
        });
    }

    private void setupStyleChangeHandler() {
        view.addThemeChangeHandler(new ChangeHandler() {

            @Override
            @SuppressWarnings("unused")
            public void onChange(final ChangeEvent event) {
                final GridRendererTheme theme = view.getSelectedTheme();
                gridWidget1.getRenderer().setTheme(theme);
                view.refresh();
            }
        });
    }

    private void setupMergedStateValueChangeHandler() {
        view.setMergedState(false);
        view.addMergedStateValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            @SuppressWarnings("unused")
            public void onValueChange(final ValueChangeEvent<Boolean> event) {
                final boolean isMerged = event.getValue();
                gridWidget1.getModel().setMerged(isMerged);
                view.refresh();
            }
        });
    }

    private void setupAppendRowClickHandler() {
        view.addAppendRowClickHandler((final ClickEvent event) -> {
            for (GridWidget gridWidget : view.getGridWidgets()) {
                if (gridWidget.isSelected()) {
                    gridWidget.getModel().appendRow(new BaseGridRow());
                }
            }
            view.refresh();
        });
    }

    private void setupDeleteRowClickHandler() {
        view.addDeleteRowClickHandler((final ClickEvent event) -> {
            for (GridWidget gridWidget : view.getGridWidgets()) {
                if (gridWidget.isSelected()) {
                    if (gridWidget.getModel().getRowCount() > 0) {
                        gridWidget.getModel().deleteRow(0);
                    }
                }
            }
            view.refresh();
        });
    }

    private void clearCells() {
        final GridWidget selectedGridWidget = getSelectedGridWidget();
        if (selectedGridWidget == null) {
            return;
        }
        final GridData gridModel = selectedGridWidget.getModel();
        final List<GridData.SelectedCell> selectedCells = gridModel.getSelectedCells();
        for (GridData.SelectedCell cell : selectedCells) {
            gridModel.deleteCell(cell.getRowIndex(),
                                 cell.getColumnIndex());
        }
        view.refresh();
    }

    private GridWidget getSelectedGridWidget() {
        for (GridWidget gridWidget : view.getGridWidgets()) {
            if (gridWidget.isSelected()) {
                return gridWidget;
            }
        }
        return null;
    }

    @Override
    public void select(final GridWidget selectedGridWidget) {
        view.select(selectedGridWidget);
        final boolean hasSelections = selectedGridWidget.getModel().getSelectedCells().size() > 0;
        menus.getItems().get(0).setEnabled(hasSelections);
        menus.getItems().get(1).setEnabled(hasSelections);
    }

    @Override
    public void selectLinkedColumn(final GridColumn<?> selectedGridColumn) {
        view.selectLinkedColumn(selectedGridColumn);
    }

    @Override
    public Set<GridWidget> getGridWidgets() {
        return view.getGridWidgets();
    }

    public void addGridColumnGroup(GridData grid, String groupName, String... columnNames) {

        Arrays.stream(columnNames).forEach(name -> {
            final GridColumn.HeaderMetaData gridColumnHeaderMetaData1 = new BaseHeaderMetaData(groupName,
                                                                                                "gridColumnGroup");
            final GridColumn.HeaderMetaData gridColumnHeaderMetaData2 = new BaseHeaderMetaData(name,
                                                                                                groupName);
            final List<GridColumn.HeaderMetaData> grid1ColumnHeaderMetaData = new ArrayList<GridColumn.HeaderMetaData>();
            grid1ColumnHeaderMetaData.add(gridColumnHeaderMetaData1);
            grid1ColumnHeaderMetaData.add(gridColumnHeaderMetaData2);
            final BaseGridColumn<String> gridColumn = new StringPopupColumn(grid1ColumnHeaderMetaData,
                                                                             new StringColumnRenderer(),
                                                                             100);
            gridColumn.setMinimumWidth(50.0);
            grid.appendColumn(gridColumn);
        });
    }
}
