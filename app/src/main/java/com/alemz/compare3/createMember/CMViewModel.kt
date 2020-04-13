package com.alemz.compare3.createMember

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.repositories.MemberRepository

class CMViewModel (application: Application): AndroidViewModel(application) {
    private var memberRepository: MemberRepository = MemberRepository(application)

    fun insertMember(member: FamilyMember){
        memberRepository.insert(member)
    }


}