package com.huhn.androidarchitectureexample.repository.remoteDataSource.networkModel



data class EnhancedDateItem(
    val attitude_quaternions: AttitudeQuaternions,
    val caption: String,
    val centroid_coordinates: CentroidCoordinates,
    val coords: Coords,
    val date: String,
    val dscovr_j2000_position: DscovrJ2000PositionX,
    val identifier: String,
    val image: String,
    val lunar_j2000_position: LunarJ2000PositionX,
    val sun_j2000_position: SunJ2000PositionX,
    val version: String
)

data class AttitudeQuaternions(
    val q0: Double,
    val q1: Double,
    val q2: Double,
    val q3: Double
)

data class CentroidCoordinates(
    val lat: Double,
    val lon: Double
)

data class Coords(
    val attitude_quaternions: AttitudeQuaternions,
)

data class DscovrJ2000PositionX(
    val x: Double,
    val y: Double,
    val z: Double
)

data class LunarJ2000PositionX(
    val x: Double,
    val y: Double,
    val z: Double
)

data class SunJ2000PositionX(
    val x: Double,
    val y: Double,
    val z: Double
)