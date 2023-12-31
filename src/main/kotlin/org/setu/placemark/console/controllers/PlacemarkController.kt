package org.setu.placemark.console.controllers

import mu.KotlinLogging
import org.setu.placemark.console.models.PlacemarkJSONStore
import org.setu.placemark.console.models.PlacemarkMemStore
import org.setu.placemark.console.models.PlacemarkModel
import org.setu.placemark.console.views.PlacemarkView

class PlacemarkController {

    val placemarks = PlacemarkJSONStore()
    // val placemarks = PlacemarkMemStore()
    val placemarkView = PlacemarkView()
    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Launching Placemark Console App" }
        println("Placemark Kotlin App Version 4.0")
    }

    fun start() {
        var input: Int
        do {
            input = placemarkView.menu()
            when(input) {
                1 -> add()
                2 -> update()
                3 -> list()
                4 -> searchPlacemarks()
                5 -> delete()
                -99 -> dummyData()
                -1 -> println("Exiting Placemark App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting down Placemark Console App" }
    }

    fun menu(): Int {
        return placemarkView.menu()
    }

    fun add() {
        val addPlacemark = PlacemarkModel()
        if (placemarkView.addPlacemarkData(addPlacemark)) {
            placemarks.create(addPlacemark)
        } else {
            logger.info("Placemark Not Added")
        }
    }

    fun list() {
        placemarkView.listPlacemarks(placemarks)
    }

    fun update() {
        placemarkView.listPlacemarks(placemarks)
        var searchId = placemarkView.getId()
        val updatePlacemark = search(searchId)
        if (updatePlacemark != null) {
            if (placemarkView.updatePlacemarkData(updatePlacemark)) {
                placemarks.update(updatePlacemark)
                placemarkView.showPlacemark(updatePlacemark)
                logger.info("Placemark Updated: $updatePlacemark")
            }
            else
                logger.info("Placemark Not Updated")
        } else {
            logger.info("Placemark Not Updated")
        }
    }

    fun searchPlacemarks() {
        val foundPlacemark = search(placemarkView.getId())!!
        placemarkView.showPlacemark(foundPlacemark)
    }

    fun search(id: Long) : PlacemarkModel? {
        var foundPlacemark = placemarks.findOne(id)
        return foundPlacemark
    }

    fun delete() {
        placemarkView.listPlacemarks(placemarks)
        var searchId = placemarkView.getId()
        val deletePlacemark = search(searchId)

        if (deletePlacemark != null) {
            placemarks.delete(deletePlacemark)
            println("Placemark Deleted")
            placemarkView.listPlacemarks(placemarks)
        } else {
            println("Placemark Not Deleted")
        }
    }

    fun dummyData() {
        placemarks.create(PlacemarkModel(title = "Ballyfermot", description = "Home of \"Lam's\" and \"New Lam's\""))
        placemarks.create(PlacemarkModel(title = "Chessington", description = "It's a World of Adventures!"))
        placemarks.create(PlacemarkModel(title = "Brussels", description = "Bruxelles ma belle !"))
        placemarks.create(PlacemarkModel(title = "Berlin", description = "Das war die schönste Zeit!"))
    }
}