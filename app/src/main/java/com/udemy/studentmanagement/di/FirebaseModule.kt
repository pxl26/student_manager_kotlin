package com.udemy.studentmanagement.di

import com.udemy.studentmanagement.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    fun provideStudentDatabase(): StudentFirebase {
        return StudentFirebase
    }

    @Provides
    fun provideClassDatabase(): ClassFirebase {
        return ClassFirebase
    }

    @Provides
    fun provideTranscriptDatabase(): TranscriptFirebase {
        return TranscriptFirebase
    }

    @Provides
    fun provideSettingDatabase(): SettingFirebase {
        return SettingFirebase
    }

    @Provides
    fun provideSummaryFirebase(): SummaryFirebase {
        return SummaryFirebase
    }
}
