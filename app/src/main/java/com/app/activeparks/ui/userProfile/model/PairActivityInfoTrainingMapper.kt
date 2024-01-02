package com.app.activeparks.ui.userProfile.model

/**
 * Created by O.Dziuba on 26.12.2023.
 */
class PairActivityInfoTrainingMapper {

    companion object {
        fun map(listFirst: List<ActivityInfoStatisticTrainingItem>, listSecond: List<ActivityInfoStatisticTrainingItem>): List<PairActivityInfoTrainingItem> {
            val resList = mutableListOf<PairActivityInfoTrainingItem>()

            if (listFirst.size == listSecond.size) {
                listFirst.forEachIndexed { index, item ->
                    resList.add(PairActivityInfoTrainingItem(
                        item.id,
                        item.number,
                        listSecond[index].number,
                        item.description,
                        item.unit,
                        item.img,
                        item.isSelected,
                        item.isPulseGadget,
                        item.isOutside
                    ))
                }
            } else {
                listFirst.forEach {
                    resList.add(PairActivityInfoTrainingItem(
                        it.id,
                        it.number,
                        "",
                        it.description,
                        it.unit,
                        it.img,
                        it.isSelected,
                        it.isPulseGadget,
                        it.isOutside
                    ))
                }
            }

            return resList
        }
    }
}