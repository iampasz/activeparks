package com.app.activeparks.ui.track.model

import com.technodreams.activeparks.R

class RoadSurface (
    val id: String,
    var title: String,
    var description: String
    ) {

        companion object {
            fun getRoadSurface() = listOf(
                RoadSurface("625d2bd5-f045-4341-a2e1-e53ef1f49933","Асфальт","Гладка поверхня, часто знаходиться на дорогах і тротуарах."),
                RoadSurface("625d2bd5-f045-4341-a2e1-e53ef1f49936","Газон","Трав'яний покрив, де бігачі можуть вправлятися в бігу на м'якому газоні."),
                RoadSurface("625d2bd5-f045-4341-a2e1-e53ef1f49935","Грунт", "Наприклад, лісові шляхи або траси для кросового бігу, що можуть бути покриті гравієм, травою, грунтом тощо."),
                RoadSurface("625d2bd5-f045-4341-a2e1-e53ef1f49934","Паркова доріжка", "Зазвичай покриття з бігу по деревах і природі, часто більш м'яка і менш навантажена на суглоби."),
                RoadSurface("625d2bd5-f045-4341-a2e1-e53ef1f49937","Трек", "Спеціалізована доріжка для бігу на стадіонах зі спеціальним покриттям, таким як каучукові доріжки."),
            )
        }
}
