package com.alemz.compare3.newCompare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.data.Similarity
import com.alemz.compare3.repositories.MemberRepository
import com.alemz.compare3.repositories.SimilarityRepository

class NCViewModel (application: Application): AndroidViewModel(application){
    private var similarityRepository: SimilarityRepository = SimilarityRepository(application)
    private var memberRepository: MemberRepository = MemberRepository(application)

    fun insertMember(member: Similarity){
        similarityRepository.insert(member)
    }

    fun getList(mode: String): List<String> {
        return memberRepository.getList(mode)
    }
    fun getAllNoLive(): List<FamilyMember> {
        return memberRepository.getAllNoLive()
    }
}
