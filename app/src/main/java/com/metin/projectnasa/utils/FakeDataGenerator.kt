package com.metin.projectnasa.utils

import com.metin.projectnasa.data.model.Camera
import com.metin.projectnasa.data.model.Photo
import com.metin.projectnasa.data.model.Rover

object FakeDataGenerator {
    private val testModel =
        Photo(
            Camera(
                "Front Hazard Avoidance Camera",
                20,
                "FHAZ",
                5
            ),
            "2015-05-30",
            102693,
            Constants.TEST_IMAGE_URL,
            Rover(5, "2012-08-06", "2011-11-26", "Curiosity", "active"),
            1000
        )

    private val testModel2 =
        Photo(
            Camera(
                "Front Hazard Avoidance Camera",
                20,
                "RHAZ",
                5
            ),
            "2015-05-30",
            102693,
            Constants.TEST_IMAGE_URL,
            Rover(5, "2012-08-06", "2011-11-26", "Curiosity", "active"),
            1000
        )

    private val testModel3 =
        Photo(
            Camera(
                "Front Hazard Avoidance Camera",
                20,
                "RHAZ",
                5
            ),
            "2015-05-30",
            102693,
            Constants.TEST_IMAGE_URL,
            Rover(5, "2012-08-06", "2011-11-26", "Curiosity", "active"),
            1000
        )

    private val testModel4 =
        Photo(
            Camera(
                "Front Hazard Avoidance Camera",
                20,
                "NAVCAM",
                5
            ),
            "2015-05-30",
            102693,
            Constants.TEST_IMAGE_URL,
            Rover(5, "2012-08-06", "2011-11-26", "Curiosity", "active"),
            1000
        )

    val photos = mutableListOf(
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4,
        testModel2,
        testModel3,
        testModel4,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4,
        testModel2,
        testModel3,
        testModel4,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4
    )

    val photos2 = mutableListOf(
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel,
        testModel2,
        testModel3,
        testModel4
    )
}