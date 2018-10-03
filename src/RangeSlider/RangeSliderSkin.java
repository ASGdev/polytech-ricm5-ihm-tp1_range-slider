package RangeSlider;

import javafx.animation.Transition;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;

/**
 * Region/css based skin for Slider
 */
public class RangeSliderSkin extends BehaviorSkinBase<RangeSlider, RangeSliderBehavior> {

	/** Track if slider is vertical/horizontal and cause re layout */
	// private boolean horizontal;
	private NumberAxis tickLine = null;
	private double trackToTickGap = 2;

	private boolean showTickMarks;
	private double thumbWidth;
	private double thumbHeight;

	private double trackStart;
	private double trackLength;
	private double thumbTop;
	private double thumbLeft;
	private double preDragThumbPos;
	private Point2D dragStart; // in skin coordinates

	private StackPane inf_thumb;
	private StackPane sup_thumb;

	private SelectedThumb currently_selected_thumb;

	private StackPane track;
	
	private double rangeTrackerOriginPosition;

	// track between two ranges
	private StackPane rangeTrack;
	private boolean rangeTrackClicked = false;

	private boolean trackClicked = false;
	// private double visibleAmount = 16;

	public RangeSliderSkin(RangeSlider rangeslider) {
		super(rangeslider, new RangeSliderBehavior(rangeslider));

		initialize();
		rangeslider.requestLayout();
		registerChangeListener(rangeslider.minProperty(), "MIN");
		registerChangeListener(rangeslider.maxProperty(), "MAX");
		registerChangeListener(rangeslider.infValueProperty(), "INF_VALUE");
		registerChangeListener(rangeslider.supValueProperty(), "SUP_VALUE");
		registerChangeListener(rangeslider.orientationProperty(), "ORIENTATION");
		registerChangeListener(rangeslider.showTickMarksProperty(), "SHOW_TICK_MARKS");
		registerChangeListener(rangeslider.showTickLabelsProperty(), "SHOW_TICK_LABELS");
		registerChangeListener(rangeslider.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
		registerChangeListener(rangeslider.minorTickCountProperty(), "MINOR_TICK_COUNT");
		registerChangeListener(rangeslider.labelFormatterProperty(), "TICK_LABEL_FORMATTER");
		registerChangeListener(rangeslider.snapToTicksProperty(), "SNAP_TO_TICKS");
	}

	private void initialize() {
		inf_thumb = new StackPane();
		inf_thumb.getStyleClass().setAll("thumb");
		track = new StackPane();
		track.getStyleClass().setAll("track");
		// horizontal = getSkinnable().isVertical();

		rangeTrack = new StackPane();
		rangeTrack.getStyleClass().setAll("track");
		// sets default color for range (inner) track
		rangeTrack.setStyle("-fx-background-color: slateblue;");

		sup_thumb = new StackPane();
		sup_thumb.getStyleClass().setAll("thumb");

		getChildren().clear();
		getChildren().addAll(track, rangeTrack, sup_thumb, inf_thumb);

		setShowTickMarks(getSkinnable().isShowTickMarks(), getSkinnable().isShowTickLabels());
		track.setOnMousePressed(me -> {
			if (!inf_thumb.isPressed() || !sup_thumb.isPressed()) {
				trackClicked = true;
				if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
					getBehavior().trackPress(me, (me.getX() / trackLength), currently_selected_thumb);
				} else {
					getBehavior().trackPress(me, (me.getY() / trackLength), currently_selected_thumb);
				}
				trackClicked = false;
			}
		});

		track.setOnMouseDragged(me -> {
			if (!inf_thumb.isPressed() || !sup_thumb.isPressed()) {
				if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
					getBehavior().trackPress(me, (me.getX() / trackLength), currently_selected_thumb);
				} else {
					getBehavior().trackPress(me, (me.getY() / trackLength), currently_selected_thumb);
				}
			}
		});
		
		rangeTrack.setOnMousePressed(me -> {
			if (!inf_thumb.isPressed() || !sup_thumb.isPressed()) {
				rangeTrackClicked = true;
				if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
					// sets original position
					//rangeTrackerOriginPosition = (me.getX() / trackLength);
					//System.out.println("setting origin pos : " + rangeTrackerOriginPosition);
					
					getBehavior().trackPress(me, (me.getX() / trackLength), currently_selected_thumb);
				} else {
					//getBehavior().trackPress(me, (me.getY() / trackLength), currently_selected_thumb);
				}
				rangeTrackClicked = false;
			}
		});

		rangeTrack.setOnMouseDragged(me -> {
			if (!inf_thumb.isPressed() || !sup_thumb.isPressed()) {
				if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
					getBehavior().trackPress(me, (me.getX() / trackLength), currently_selected_thumb);
					getBehavior().rangeTrackPress(me, (me.getX() / trackLength), rangeTrackerOriginPosition);
				} else {
					getBehavior().trackPress(me, (me.getY() / trackLength), currently_selected_thumb);
					getBehavior().rangeTrackPress(me, (me.getY() / trackLength), rangeTrackerOriginPosition);
				}
			}
		});

		inf_thumb.setOnMousePressed(me -> {
			System.out.println("selecting inf_thumb");
			currently_selected_thumb = SelectedThumb.INF;
			getBehavior().infThumbPressed(me, 0.0f);
			dragStart = inf_thumb.localToParent(me.getX(), me.getY());
			preDragThumbPos = (getSkinnable().getInfValue() - getSkinnable().getMin())
					/ (getSkinnable().getMax() - getSkinnable().getMin());
		});

		inf_thumb.setOnMouseReleased(me -> {
			getBehavior().infThumbReleased(me);
		});

		inf_thumb.setOnMouseDragged(me -> {
			Point2D cur = inf_thumb.localToParent(me.getX(), me.getY());
			double dragPos = (getSkinnable().getOrientation() == Orientation.HORIZONTAL) ? cur.getX() - dragStart.getX()
					: -(cur.getY() - dragStart.getY());
			getBehavior().infThumbDragged(me, preDragThumbPos + dragPos / trackLength);
		});

		sup_thumb.setOnMousePressed(me -> {
			System.out.println("selecting sup_thumb");
			currently_selected_thumb = SelectedThumb.SUP;
			getBehavior().supThumbPressed(me, 0.0f);
			dragStart = sup_thumb.localToParent(me.getX(), me.getY());
			preDragThumbPos = (getSkinnable().getSupValue() - getSkinnable().getMin())
					/ (getSkinnable().getMax() - getSkinnable().getMin());
		});

		sup_thumb.setOnMouseReleased(me -> {
			getBehavior().supThumbReleased(me);
		});

		sup_thumb.setOnMouseDragged(me -> {
			Point2D cur = sup_thumb.localToParent(me.getX(), me.getY());
			double dragPos = (getSkinnable().getOrientation() == Orientation.HORIZONTAL) ? cur.getX() - dragStart.getX()
					: -(cur.getY() - dragStart.getY());
			getBehavior().supThumbDragged(me, preDragThumbPos + dragPos / trackLength);
		});
	}

	StringConverter<Number> stringConverterWrapper = new StringConverter<Number>() {
		RangeSlider rangeslider = getSkinnable();

		@Override
		public String toString(Number object) {
			return (object != null) ? rangeslider.getLabelFormatter().toString(object.doubleValue()) : "";
		}

		@Override
		public Number fromString(String string) {
			return rangeslider.getLabelFormatter().fromString(string);
		}
	};

	private void setShowTickMarks(boolean ticksVisible, boolean labelsVisible) {
		showTickMarks = (ticksVisible || labelsVisible);
		RangeSlider slider = getSkinnable();
		if (showTickMarks) {
			if (tickLine == null) {
				tickLine = new NumberAxis();
				tickLine.setAutoRanging(false);
				tickLine.setSide(slider.getOrientation() == Orientation.VERTICAL ? Side.RIGHT
						: (slider.getOrientation() == null) ? Side.RIGHT : Side.BOTTOM);
				tickLine.setUpperBound(slider.getMax());
				tickLine.setLowerBound(slider.getMin());
				tickLine.setTickUnit(slider.getMajorTickUnit());
				tickLine.setTickMarkVisible(ticksVisible);
				tickLine.setTickLabelsVisible(labelsVisible);
				tickLine.setMinorTickVisible(ticksVisible);
				// add 1 to the slider minor tick count since the axis draws one
				// less minor ticks than the number given.
				tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
				if (slider.getLabelFormatter() != null) {
					tickLine.setTickLabelFormatter(stringConverterWrapper);
				}
				getChildren().clear();
				getChildren().addAll(tickLine, track, rangeTrack, inf_thumb, sup_thumb);
			} else {
				tickLine.setTickLabelsVisible(labelsVisible);
				tickLine.setTickMarkVisible(ticksVisible);
				tickLine.setMinorTickVisible(ticksVisible);
			}
		} else {
			getChildren().clear();
			getChildren().addAll(track, rangeTrack, inf_thumb, sup_thumb);
			// tickLine = null;
		}

		getSkinnable().requestLayout();
	}

	@Override
	protected void handleControlPropertyChanged(String p) {
		super.handleControlPropertyChanged(p);
		RangeSlider slider = getSkinnable();
		if ("ORIENTATION".equals(p)) {
			if (showTickMarks && tickLine != null) {
				tickLine.setSide(slider.getOrientation() == Orientation.VERTICAL ? Side.RIGHT
						: (slider.getOrientation() == null) ? Side.RIGHT : Side.BOTTOM);
			}
			getSkinnable().requestLayout();
		} else if ("INF_VALUE".equals(p)) {
			// only animate thumb if the track was clicked - not if the thumb is dragged
			if (currently_selected_thumb == SelectedThumb.INF) {
				positionInfThumb(trackClicked);
			} else {
				positionSupThumb(trackClicked);
			}
		} else if ("SUP_VALUE".equals(p)) {
			// only animate thumb if the track was clicked - not if the thumb is dragged
			// only animate thumb if the track was clicked - not if the thumb is dragged
			if (currently_selected_thumb == SelectedThumb.INF) {
				positionInfThumb(trackClicked);
			} else {
				positionSupThumb(trackClicked);
			}
		} else if ("MIN".equals(p)) {
			if (showTickMarks && tickLine != null) {
				tickLine.setLowerBound(slider.getMin());
			}
			getSkinnable().requestLayout();
		} else if ("MAX".equals(p)) {
			if (showTickMarks && tickLine != null) {
				tickLine.setUpperBound(slider.getMax());
			}
			getSkinnable().requestLayout();
		} else if ("SHOW_TICK_MARKS".equals(p) || "SHOW_TICK_LABELS".equals(p)) {
			setShowTickMarks(slider.isShowTickMarks(), slider.isShowTickLabels());
		} else if ("MAJOR_TICK_UNIT".equals(p)) {
			if (tickLine != null) {
				tickLine.setTickUnit(slider.getMajorTickUnit());
				getSkinnable().requestLayout();
			}
		} else if ("MINOR_TICK_COUNT".equals(p)) {
			if (tickLine != null) {
				tickLine.setMinorTickCount(Math.max(slider.getMinorTickCount(), 0) + 1);
				getSkinnable().requestLayout();
			}
		} else if ("TICK_LABEL_FORMATTER".equals(p)) {
			if (tickLine != null) {
				if (slider.getLabelFormatter() == null) {
					tickLine.setTickLabelFormatter(null);
				} else {
					tickLine.setTickLabelFormatter(stringConverterWrapper);
					tickLine.requestAxisLayout();
				}
			}
		} else if ("SNAP_TO_TICKS".equals(p)) {
			// TODO: add sup support
			slider.adjustInfValue(slider.getInfValue());
			slider.adjustSupValue(slider.getSupValue());
		}
	}

	/**
	 * Called when ever either min, max or value changes, so thumb's layoutX, Y is
	 * recomputed.
	 */
	void positionThumb(final boolean animate) {
		positionInfThumb(animate);
		positionSupThumb(animate);
	}

	void positionInfThumb(final boolean animate) {
		RangeSlider s = getSkinnable();
		if (s.getInfValue() > s.getMax())
			return;// this can happen if we are bound to something
		boolean horizontal = s.getOrientation() == Orientation.HORIZONTAL;
		final double endX = (horizontal) ? trackStart
				+ (((trackLength * ((s.getInfValue() - s.getMin()) / (s.getMax() - s.getMin()))) - thumbWidth / 2))
				: thumbLeft;
		final double endY = (horizontal) ? thumbTop
				: snappedTopInset() + trackLength
						- (trackLength * ((s.getInfValue() - s.getMin()) / (s.getMax() - s.getMin()))); // -
																										// thumbHeight/2

		if (animate) {
			// lets animate the thumb transition
			final double startX = inf_thumb.getLayoutX();
			final double startY = inf_thumb.getLayoutY();
			Transition transition = new Transition() {
				{
					setCycleDuration(Duration.millis(200));
				}

				@Override
				protected void interpolate(double frac) {
					if (!Double.isNaN(startX)) {
						inf_thumb.setLayoutX(startX + frac * (endX - startX));
					}
					if (!Double.isNaN(startY)) {
						inf_thumb.setLayoutY(startY + frac * (endY - startY));
					}
				}
			};
			transition.play();
		} else {
			inf_thumb.setLayoutX(endX);
			inf_thumb.setLayoutY(endY);
		}
	}

	/**
	 * Called when ever either min, max or value changes, so thumb's layoutX, Y is
	 * recomputed.
	 */
	void positionSupThumb(final boolean animate) {
		RangeSlider s = getSkinnable();
		if (s.getSupValue() > s.getMax())
			return;// this can happen if we are bound to something
		boolean horizontal = s.getOrientation() == Orientation.HORIZONTAL;
		final double endX = (horizontal) ? trackStart
				+ (((trackLength * ((s.getSupValue() - s.getMin()) / (s.getMax() - s.getMin()))) - thumbWidth / 2))
				: thumbLeft;
		final double endY = (horizontal) ? thumbTop
				: snappedTopInset() + trackLength
						- (trackLength * ((s.getSupValue() - s.getMin()) / (s.getMax() - s.getMin()))); // -
																										// thumbHeight/2

		if (animate) {
			// lets animate the thumb transition
			final double startX = sup_thumb.getLayoutX();
			final double startY = sup_thumb.getLayoutY();
			Transition transition = new Transition() {
				{
					setCycleDuration(Duration.millis(200));
				}

				@Override
				protected void interpolate(double frac) {
					if (!Double.isNaN(startX)) {
						sup_thumb.setLayoutX(startX + frac * (endX - startX));
					}
					if (!Double.isNaN(startY)) {
						sup_thumb.setLayoutY(startY + frac * (endY - startY));
					}
				}
			};
			transition.play();
		} else {
			sup_thumb.setLayoutX(endX);
			sup_thumb.setLayoutY(endY);
		}
	}

	@Override
	protected void layoutChildren(final double x, final double y, final double w, final double h) {
		// calculate the available space
		// resize thumb to preferred size
		thumbWidth = snapSize(inf_thumb.prefWidth(-1));
		thumbHeight = snapSize(inf_thumb.prefHeight(-1));
		inf_thumb.resize(thumbWidth, thumbHeight);
		sup_thumb.resize(thumbWidth, thumbHeight);
		// we are assuming the is common radius's for all corners on the track
		double trackRadius = track.getBackground() == null ? 0
				: track.getBackground().getFills().size() > 0
						? track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius()
						: 0;

		if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
			// track layout
			double tickLineHeight = (showTickMarks) ? tickLine.prefHeight(-1) : 0;
			double trackHeight = snapSize(track.prefHeight(-1));
			double trackAreaHeight = Math.max(trackHeight, thumbHeight);
			double totalHeightNeeded = trackAreaHeight + ((showTickMarks) ? trackToTickGap + tickLineHeight : 0);
			double startY = y + ((h - totalHeightNeeded) / 2); // center slider in available height vertically
			trackLength = snapSize(w - thumbWidth);
			trackStart = snapPosition(x + (thumbWidth / 2));
			double trackTop = (int) (startY + ((trackAreaHeight - trackHeight) / 2));
			thumbTop = (int) (startY + ((trackAreaHeight - thumbHeight) / 2));

			positionThumb(false);
			// layout track
			track.resizeRelocate((int) (trackStart - trackRadius), trackTop,
					(int) (trackLength + trackRadius + trackRadius), trackHeight);
			// layout tick line
			if (showTickMarks) {
				tickLine.setLayoutX(trackStart);
				tickLine.setLayoutY(trackTop + trackHeight + trackToTickGap);
				tickLine.resize(trackLength, tickLineHeight);
				tickLine.requestAxisLayout();
			} else {
				if (tickLine != null) {
					tickLine.resize(0, 0);
					tickLine.requestAxisLayout();
				}
				tickLine = null;
			}
			
			// rangetrack layout
			// the +5 (arbitrary) is because of the rounded thumbs on the default styling
			rangeTrack.resizeRelocate((int) (inf_thumb.getLayoutX() + 5), trackTop,
					(int) (sup_thumb.getLayoutX() - inf_thumb.getLayoutX()), (int) trackHeight);
			
		} else {
			double tickLineWidth = (showTickMarks) ? tickLine.prefWidth(-1) : 0;
			double trackWidth = snapSize(track.prefWidth(-1));
			double trackAreaWidth = Math.max(trackWidth, thumbWidth);
			double totalWidthNeeded = trackAreaWidth + ((showTickMarks) ? trackToTickGap + tickLineWidth : 0);
			double startX = x + ((w - totalWidthNeeded) / 2); // center slider in available width horizontally
			trackLength = snapSize(h - thumbHeight);
			trackStart = snapPosition(y + (thumbHeight / 2));
			double trackLeft = (int) (startX + ((trackAreaWidth - trackWidth) / 2));
			thumbLeft = (int) (startX + ((trackAreaWidth - thumbWidth) / 2));

			positionThumb(false);
			// layout track
			track.resizeRelocate(trackLeft, (int) (trackStart - trackRadius), trackWidth,
					(int) (trackLength + trackRadius + trackRadius));
			// layout tick line
			if (showTickMarks) {
				tickLine.setLayoutX(trackLeft + trackWidth + trackToTickGap);
				tickLine.setLayoutY(trackStart);
				tickLine.resize(tickLineWidth, trackLength);
				tickLine.requestAxisLayout();
			} else {
				if (tickLine != null) {
					tickLine.resize(0, 0);
					tickLine.requestAxisLayout();
				}
				tickLine = null;
			}
		}
	}

	double minTrackLength() {
		return 2 * inf_thumb.prefWidth(-1);
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		final RangeSlider s = getSkinnable();
		if (s.getOrientation() == Orientation.HORIZONTAL) {
			return (leftInset + minTrackLength() + inf_thumb.minWidth(-1) + rightInset);
		} else {
			return (leftInset + inf_thumb.prefWidth(-1) + rightInset);
		}
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		final RangeSlider s = getSkinnable();
		if (s.getOrientation() == Orientation.HORIZONTAL) {
			double axisHeight = showTickMarks ? (tickLine.prefHeight(-1) + trackToTickGap) : 0;
			return topInset + inf_thumb.prefHeight(-1) + axisHeight + bottomInset;
		} else {
			return topInset + minTrackLength() + inf_thumb.prefHeight(-1) + bottomInset;
		}
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		final RangeSlider s = getSkinnable();
		if (s.getOrientation() == Orientation.HORIZONTAL) {
			if (showTickMarks) {
				return Math.max(140, tickLine.prefWidth(-1));
			} else {
				return 140;
			}
		} else {
			double axisWidth = showTickMarks ? (tickLine.prefWidth(-1) + trackToTickGap) : 0;
			return leftInset + Math.max(inf_thumb.prefWidth(-1), track.prefWidth(-1)) + axisWidth + rightInset;
		}
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		final RangeSlider s = getSkinnable();
		if (s.getOrientation() == Orientation.HORIZONTAL) {
			return topInset + Math.max(inf_thumb.prefHeight(-1), track.prefHeight(-1))
					+ ((showTickMarks) ? (trackToTickGap + tickLine.prefHeight(-1)) : 0) + bottomInset;
		} else {
			if (showTickMarks) {
				return Math.max(140, tickLine.prefHeight(-1));
			} else {
				return 140;
			}
		}
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
			return Double.MAX_VALUE;
		} else {
			return getSkinnable().prefWidth(-1);
		}
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		if (getSkinnable().getOrientation() == Orientation.HORIZONTAL) {
			return getSkinnable().prefHeight(width);
		} else {
			return Double.MAX_VALUE;
		}
	}
}
