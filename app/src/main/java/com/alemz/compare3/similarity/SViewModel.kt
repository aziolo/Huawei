package com.alemz.compare3.similarity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.data.Similarity
import com.alemz.compare3.repositories.MemberRepository
import com.alemz.compare3.repositories.SimilarityRepository

class SViewModel(application: Application): AndroidViewModel(application) {
    private var memberRepository: MemberRepository = MemberRepository(application)
    private var similarityRepository: SimilarityRepository = SimilarityRepository(application)

    fun getAllMembers(): LiveData<List<FamilyMember>> {
        return memberRepository.getAll()
    }
    fun getAllSimilarity(): LiveData<List<Similarity>>{
        return similarityRepository.getAll()
    }
}