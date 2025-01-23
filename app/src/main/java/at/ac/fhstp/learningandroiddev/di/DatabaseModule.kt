package at.ac.fhstp.learningandroiddev.di

import android.content.Context
import androidx.room.Room
import at.ac.fhstp.learningandroiddev.data.ExpenseDatabase
import at.ac.fhstp.learningandroiddev.data.TripDao
import at.ac.fhstp.learningandroiddev.data.ExpenseDao
import at.ac.fhstp.learningandroiddev.data.ParticipantDao
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_database"
        ).build()
    }

    @Provides
    fun provideTripDao(database: ExpenseDatabase): TripDao = database.tripDao()

    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao = database.expenseDao()

    @Provides
    fun provideParticipantDao(database: ExpenseDatabase): ParticipantDao = database.participantDao()
}
