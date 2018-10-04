package RangeSlider;

import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.OrientedKeyBinding;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.util.Utils;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.END;
import static javafx.scene.input.KeyCode.F4;
import static javafx.scene.input.KeyCode.HOME;
import static javafx.scene.input.KeyCode.KP_DOWN;
import static javafx.scene.input.KeyCode.KP_LEFT;
import static javafx.scene.input.KeyCode.KP_RIGHT;
import static javafx.scene.input.KeyCode.KP_UP;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;

public class RangeSliderBehavior extends BehaviorBase<RangeSlider> {
	/**************************************************************************
	 * Setup KeyBindings * * We manually specify the focus traversal keys because
	 * Slider has * different usage for up/down arrow keys. *
	 *************************************************************************/
	protected static final List<KeyBinding> SLIDER_BINDINGS = new ArrayList<KeyBinding>();
	static {
		SLIDER_BINDINGS.add(new KeyBinding(F4, "TraverseDebug").alt().ctrl().shift());

		// controls the inferior boundary
		SLIDER_BINDINGS.add(new SliderKeyBinding(LEFT, "DecrementInfValue"));
		SLIDER_BINDINGS.add(new SliderKeyBinding(KP_LEFT, "DecrementInfValue"));
		SLIDER_BINDINGS.add(new SliderKeyBinding(RIGHT, "IncrementInfValue"));
		SLIDER_BINDINGS.add(new SliderKeyBinding(KP_RIGHT, "IncrementInfValue"));

		// controls the upper boundary
		SLIDER_BINDINGS.add(new SliderKeyBinding(UP, "IncrementSupValue").vertical());
		SLIDER_BINDINGS.add(new SliderKeyBinding(KP_UP, "IncrementSupValue").vertical());
		SLIDER_BINDINGS.add(new SliderKeyBinding(DOWN, "DecrementSupValue").vertical());
		SLIDER_BINDINGS.add(new SliderKeyBinding(KP_DOWN, "DecrementSupValue").vertical());

		SLIDER_BINDINGS.add(new KeyBinding(HOME, KEY_RELEASED, "Home"));
		SLIDER_BINDINGS.add(new KeyBinding(END, KEY_RELEASED, "End"));
	}

	protected /* final */ String matchActionForEvent(KeyEvent e) {
		String action = super.matchActionForEvent(e);
		if (action != null) {
			if (e.getCode() == LEFT || e.getCode() == KP_LEFT) {
				if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
					action = getControl().getOrientation() == Orientation.HORIZONTAL ? "IncrementValue"
							: "DecrementValue";
				}
			} else if (e.getCode() == RIGHT || e.getCode() == KP_RIGHT) {
				if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
					action = getControl().getOrientation() == Orientation.HORIZONTAL ? "DecrementValue"
							: "IncrementValue";
				}
			}
		}
		return action;
	}

	@Override
	protected void callAction(String name) {
		if ("Home".equals(name))
			home();
		else if ("End".equals(name))
			end(); // TODO : add support for both thumbs
		else if ("IncrementInfValue".equals(name))
			incrementInfValue();
		else if ("IncrementSupValue".equals(name))
			incrementSupValue();
		else if ("DecrementInfValue".equals(name))
			decrementInfValue();
		else if ("DecrementSupValue".equals(name))
			decrementSupValue();
		else
			super.callAction(name);
	}

	private TwoLevelFocusBehavior tlFocus;

	public RangeSliderBehavior(RangeSlider rangeslider) {
		super(rangeslider, SLIDER_BINDINGS);
		// Only add this if we're on an embedded platform that supports 5-button
		// navigation
		if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
			tlFocus = new TwoLevelFocusBehavior(rangeslider); // needs to be last.
		}
	}

	@Override
	public void dispose() {
		if (tlFocus != null)
			tlFocus.dispose();
		super.dispose();
	}

	/**************************************************************************
	 * State and Functions *
	 *************************************************************************/

	/**
	 * Invoked by the Slider {@link Skin} implementation whenever a mouse press
	 * occurs on the "track" of the slider.
	 * 
	 * This will cause the currently selected thumb to be moved by some amount.
	 *
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void trackPress(MouseEvent e, double position, SelectedThumb target_thumb) {
		System.out.println("track pressed");
		// determine the percentage of the way between min and max
		// represented by this mouse event
		final RangeSlider rangeslider = getControl();
		// If not already focused, request focus
		if (!rangeslider.isFocused())
			rangeslider.requestFocus();
		if (rangeslider.getOrientation().equals(Orientation.HORIZONTAL)) {
			if (target_thumb == SelectedThumb.INF) {
				double newvalue = position * (rangeslider.getMax() - rangeslider.getMin()) + rangeslider.getMin();
							
				if(newvalue > rangeslider.getSupValue()) {
					rangeslider.adjustSupValue(newvalue);	
				} else {
					rangeslider.adjustInfValue(newvalue);
				}
			
			} else {
				double newvalue = position * (rangeslider.getMax() - rangeslider.getMin()) + rangeslider.getMin();
				
				if(newvalue < rangeslider.getSupValue()) {
					rangeslider.adjustInfValue(newvalue);	
				} else {
					rangeslider.adjustSupValue(newvalue);
				}
				
			}
		} else {
			if (target_thumb == SelectedThumb.INF) {
				rangeslider.adjustInfValue(
						(1 - position) * (rangeslider.getMax() - rangeslider.getMin()) + rangeslider.getMin());
			} else {
				rangeslider.adjustSupValue(
						position * (rangeslider.getMax() - rangeslider.getMin()) + rangeslider.getMin());
			}
		}
	}

	/**
	 * Invoked by the Slider {@link Skin} implementation whenever a mouse press
	 * occurs on the "range track" of the slider.
	 * 
	 * This will cause both thumbs to be moved by some amount.
	 *
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void rangeTrackPress(MouseEvent e, double position, double originalPosition) {
		System.out.println("rangeTrack clicked");
		// determine the percentage of the way between min and max
		// represented by this mouse event
		final RangeSlider rangeslider = getControl();
		// If not already focused, request focus
		if (!rangeslider.isFocused())
			rangeslider.requestFocus();
		if (rangeslider.getOrientation().equals(Orientation.HORIZONTAL)) {
			double dragDelta = position - originalPosition;
			System.out.println("delta " + dragDelta);
			
			if((rangeslider.getInfValue() + dragDelta >= rangeslider.getMin()) && (rangeslider.getSupValue() + dragDelta <= rangeslider.getMax())) {
				rangeslider.adjustInfValue(rangeslider.getInfValue() + dragDelta);
				rangeslider.adjustSupValue(rangeslider.getSupValue() + dragDelta);
			} 

		} else {
			// not supported yet
		
		}
		
	}

	/**
	 * Handles inf thumb event
	 * 
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void infThumbPressed(MouseEvent e, double position) {
		// If not already focused, request focus
		final RangeSlider rangeslider = getControl();
		if (!rangeslider.isFocused())
			rangeslider.requestFocus();
		rangeslider.setInfValueChanging(true);
	}

	/**
	 * Handles sup thumb event
	 * 
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void supThumbPressed(MouseEvent e, double position) {
		// If not already focused, request focus
		final RangeSlider rangeslider = getControl();
		if (!rangeslider.isFocused())
			rangeslider.requestFocus();
		rangeslider.setSupValueChanging(true);
	}

	/**
	 * Handles inf thumb event
	 * 
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void infThumbDragged(MouseEvent e, double position) {
		final RangeSlider rangeslider = getControl();

		double newvalue = Utils.clamp(rangeslider.getMin(),
				(position * (rangeslider.getMax() - rangeslider.getMin())) + rangeslider.getMin(),
				rangeslider.getMax());

		if (newvalue > rangeslider.getSupValue()) {
			rangeslider.setSupValue(newvalue);
		}

		rangeslider.setInfValue(newvalue);
	}

	/**
	 * Handles sup thumb event
	 * 
	 * @param position
	 *            The mouse position on track with 0.0 being beginning of track and
	 *            1.0 being the end
	 */
	public void supThumbDragged(MouseEvent e, double position) {
		final RangeSlider rangeslider = getControl();

		double newvalue = Utils.clamp(rangeslider.getMin(),
				(position * (rangeslider.getMax() - rangeslider.getMin())) + rangeslider.getMin(),
				rangeslider.getMax());

		if (newvalue < rangeslider.getInfValue()) {
			rangeslider.setInfValue(newvalue);
		}

		rangeslider.setSupValue(newvalue);
	}

	/**
	 * Handles inf thumb event When thumb is released valueChanging should be set to
	 * false.
	 */
	public void infThumbReleased(MouseEvent e) {
		final RangeSlider rangeslider = getControl();
		rangeslider.setInfValueChanging(false);
		// RT-15207 When snapToTicks is true, slider value calculated in drag
		// is then snapped to the nearest tick on mouse release.
		rangeslider.adjustInfValue(rangeslider.getInfValue());
	}

	/**
	 * Handles sup thumb event When thumb is released valueChanging should be set to
	 * false.
	 */
	public void supThumbReleased(MouseEvent e) {
		final RangeSlider rangeslider = getControl();
		rangeslider.setSupValueChanging(false);
		// RT-15207 When snapToTicks is true, slider value calculated in drag
		// is then snapped to the nearest tick on mouse release.
		rangeslider.adjustSupValue(rangeslider.getSupValue());
	}

	void home() {
		final RangeSlider rangeslider = getControl();
		rangeslider.adjustInfValue(rangeslider.getMin());
		rangeslider.adjustSupValue(rangeslider.getMin());
	}

	void decrementInfValue() {
		final RangeSlider rangeslider = getControl();
		// RT-8634 If snapToTicks is true and block increment is less than
		// tick spacing, tick spacing is used as the decrement value.
		if (rangeslider.isSnapToTicks()) {
			rangeslider.adjustInfValue(rangeslider.getInfValue() - computeIncrement());
		} else {
			rangeslider.decrementInf();
		}

	}

	void decrementSupValue() {
		final RangeSlider rangeslider = getControl();
		// RT-8634 If snapToTicks is true and block increment is less than
		// tick spacing, tick spacing is used as the decrement value.
		if (rangeslider.isSnapToTicks()) {
			rangeslider.adjustSupValue(rangeslider.getSupValue() - computeIncrement());
		} else {
			rangeslider.decrementSup();
		}

	}

	void end() {
		final RangeSlider rangeslider = getControl();
		rangeslider.adjustInfValue(rangeslider.getMax());
		rangeslider.adjustSupValue(rangeslider.getMin());
	}

	void incrementInfValue() {
		final RangeSlider rangeslider = getControl();
		// RT-8634 If snapToTicks is true and block increment is less than
		// tick spacing, tick spacing is used as the increment value.
		if (rangeslider.isSnapToTicks()) {
			rangeslider.adjustInfValue(rangeslider.getInfValue() + computeIncrement());
		} else {
			rangeslider.incrementInf();
		}
	}

	void incrementSupValue() {
		final RangeSlider rangeslider = getControl();
		// RT-8634 If snapToTicks is true and block increment is less than
		// tick spacing, tick spacing is used as the increment value.
		if (rangeslider.isSnapToTicks()) {
			rangeslider.adjustSupValue(rangeslider.getSupValue() + computeIncrement());
		} else {
			rangeslider.incrementSup();
		}
	}

	// Used only if snapToTicks is true.
	double computeIncrement() {
		final RangeSlider rangeslider = getControl();
		double tickSpacing = 0;
		if (rangeslider.getMinorTickCount() != 0) {
			tickSpacing = rangeslider.getMajorTickUnit() / (Math.max(rangeslider.getMinorTickCount(), 0) + 1);
		} else {
			tickSpacing = rangeslider.getMajorTickUnit();
		}

		if (rangeslider.getBlockIncrement() > 0 && rangeslider.getBlockIncrement() < tickSpacing) {
			return tickSpacing;
		}

		return rangeslider.getBlockIncrement();
	}

	public static class SliderKeyBinding extends OrientedKeyBinding {
		public SliderKeyBinding(KeyCode code, String action) {
			super(code, action);
		}

		public SliderKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
			super(code, type, action);
		}

		public @Override boolean getVertical(Control control) {
			return ((RangeSlider) control).getOrientation() == Orientation.VERTICAL;
		}
	}

}
