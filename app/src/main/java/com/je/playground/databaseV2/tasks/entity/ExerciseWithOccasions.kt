package com.je.playground.databaseV2.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class ExerciseWithOccasions(
    @Embedded var exercise : Exercise,

    @Relation(
        entity = ExerciseOccasion::class,
        parentColumn = "exercise_id",
        entityColumn = "exercise_id"
    )
    var exerciseOccasions : List<ExerciseOccasion>
)