/**
 * 
 */
//Cover on the top
var JiKengCoverMaterial = new Cesium.GridMaterialProperty({
});

viewer.entities.add({
  name : 'JiKengCover1' ,
  rectangle : {
    coordinates : Cesium.Rectangle.fromDegrees(121.61489896221692,31.122177681562143,121.61521205618894,31.122709151301755),
    height:20,
    outline : true,
    outlineColor : Cesium.Color.WHITE,
    outlineWidth : 4,
    stRotation : Cesium.Math.toRadians(45),
    material : JiKengCoverMaterial
  }
});