package com.alemz.compare3.newCompare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alemz.compare3.data.Similarity
import com.alemz.compare3.repositories.SimilarityRepository

class NCViewModel (application: Application): AndroidViewModel(application){
    private var similarityRepository: SimilarityRepository = SimilarityRepository(application)

    fun insertMember(member: Similarity){
        similarityRepository.insert(member)
    }
}
