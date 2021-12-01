package dev.slimevr.gui.views;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import dev.slimevr.gui.items.trackers.TrackerPanelCell;
import io.eiren.util.ann.ThreadSafe;
import io.eiren.util.collections.FastList;
import io.eiren.vr.VRServer;
import io.eiren.vr.trackers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TrackersListPane extends GridPane implements Initializable {

	private static final long UPDATE_DELAY = 50;
	private List<TrackerPanelCell> trackers;

	Quaternion q = new Quaternion();
	Vector3f v = new Vector3f();
	float[] angles = new float[3];

	private VRServer server;
	private long lastUpdate = 0;

	public TrackersListPane() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/gui/trackersListPane.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	public TrackersListPane(VRServer server) {

	}

	public void init(VRServer server)
	{
		this.server = server;
		this.trackers = new FastList<>();

		server.addNewTrackerConsumer(this::newTrackerAdded);
	}


	private void build() {
		getChildren().clear();
		trackers.sort((tr1, tr2) -> getTrackerSort(tr1.t) - getTrackerSort(tr2.t));
		Class<? extends Tracker> currentClass = null;
		boolean first = true;
		int row = 0;
		int column =0;
		for (int i = 0; i < trackers.size(); ++i) {
			if(column==2)
			{
				column=0;
				row++;
			}
			TrackerPanelCell tr = trackers.get(i);
			Tracker t = tr.t;
			this.add(tr.getTrackerContainer(), column, row);
			column++;
			tr.build();
		}

	}


	public void trackersListInit() {
		/*trackers.sort(Comparator.comparingInt(tr -> getTrackerSort(tr.tracker)));
		
		Class<? extends Tracker> currentClass = null;
		boolean first = true;
		
		for(int i = 0; i < trackers.size(); ++i) {

			TrackerPanelCell tr = trackers.get(i);
			Tracker t = tr.tracker;
			
			if(t instanceof ReferenceAdjustedTracker)
				t = ((ReferenceAdjustedTracker<?>) t).getTracker();
			
		}*/

	}

	@ThreadSafe
	public void updateTrackers() {
		if (lastUpdate + UPDATE_DELAY > System.currentTimeMillis())
			return;
		lastUpdate = System.currentTimeMillis();
		Platform.runLater(() -> {
			for (TrackerPanelCell tracker : trackers) tracker.update();
		});

		/*java.awt.EventQueue.invokeLater(() -> {
			for(int i = 0; i < trackers.size(); ++i)
				trackers.get(i).update();
		});*/
	}

	private int getTrackerSort(Tracker t) {
		if (t instanceof ReferenceAdjustedTracker)
			t = ((ReferenceAdjustedTracker<?>) t).getTracker();
		if (t instanceof IMUTracker)
			return 0;
		if (t instanceof HMDTracker)
			return 100;
		if (t instanceof ComputedTracker)
			return 200;
		return 1000;
	}


	@ThreadSafe
	public void newTrackerAdded(Tracker t) {
		trackers.add(new TrackerPanelCell(t, server));
		Platform.runLater(
				this::build
		);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
