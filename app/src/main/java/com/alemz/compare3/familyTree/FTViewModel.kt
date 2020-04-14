package com.alemz.compare3.familyTree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.repositories.MemberRepository

class FTViewModel (application: Application): AndroidViewModel(application)  {
    private var memberRepository: MemberRepository = MemberRepository(application)

    fun getAll(): LiveData<List<FamilyMember>> {
        return memberRepository.getAll()
    }
}