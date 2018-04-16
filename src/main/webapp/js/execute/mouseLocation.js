var scene = viewer.scene;
var handler;

Sandcastle.addDefaultToolbarButton('Show Cartographic Position on Mouse Over',
		function() {
			var entity = viewer.entities.add({
				label : {
					show : false
				}
			});

			// Mouse over the globe to see the cartographic position
			handler = new Cesium.ScreenSpaceEventHandler(scene.canvas);
			handler.setInputAction(function(movement) {
				var cartesian = viewer.camera.pickEllipsoid(
						movement.endPosition, scene.globe.ellipsoid);
				if (cartesian) {
					var cartographic = Cesium.Cartographic
							.fromCartesian(cartesian);
					var longitudeString = Cesium.Math.toDegrees(
							cartographic.longitude).toFixed(15);
					var latitudeString = Cesium.Math.toDegrees(
							cartographic.latitude).toFixed(15);

					entity.position = cartesian;
					entity.label.show = true;
					// entity.label.text = '(' + longitudeString + ', ' +
					// latitudeString + ')';
					entity.label.text = "current:"+valueToShow;
				} else {
					entity.label.show = false;
				}
			}, Cesium.ScreenSpaceEventType.MOUSE_MOVE);
		});

/*
 * Sandcastle.reset = function() { viewer.entities.removeAll(); handler =
 * handler && handler.destroy(); };
 */