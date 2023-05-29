package com.udemy.studentmanagement.di

import com.udemy.studentmanagement.database.*
import com.udemy.studentmanagement.repository.ClassRepository
import com.udemy.studentmanagement.repository.StudentRepository
import com.udemy.studentmanagement.repository.SummaryRepository
import com.udemy.studentmanagement.repository.TranscriptRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// There's only one repository exist at one time
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideStudentRepository(studentDatabase: StudentFirebase) : StudentRepository {
        return StudentRepository(studentDatabase)
    }

    @Provides
    fun provideClassRepository(studentFirebase: StudentFirebase
                               ,classFirebase : ClassFirebase): ClassRepository {
        return ClassRepository(studentFirebase,classFirebase)
    }


    @Provides
    fun provideTranscriptRepository(
        classFirebase: ClassFirebase,
        transcriptFirebase: TranscriptFirebase
    ): TranscriptRepository {
        return TranscriptRepository(classFirebase, transcriptFirebase)
    }

    @Provides
    fun provideSummaryRepository(
        summaryFirebase: SummaryFirebase,
        transcriptFirebase: TranscriptFirebase
    ): SummaryRepository {
        return SummaryRepository(summaryFirebase, transcriptFirebase)
    }

}

