package at.ac.fhstp.learningandroiddev.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Database Configuration
@Database(
    entities = [Trip::class, Expense::class, Participant::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun participantDao(): ParticipantDao
}

// Type Converters (for future extensibility)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): java.util.Date = java.util.Date(value)

    @TypeConverter
    fun toTimestamp(date: java.util.Date): Long = date.time
}

// Trip Entity
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis() // Default to current timestamp
)

// Expense Entity
@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tripId"])] // Index for faster lookups
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int, // Foreign key linking to Trip
    val description: String,
    val amount: Double,
    val paidBy: String,
    val createdAt: Long = System.currentTimeMillis()
)

// Participant Entity
@Entity(
    tableName = "participants",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tripId"])]
)
data class Participant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int, // Foreign key linking to Trip
    val name: String
)

// DAO Interfaces

// Trip DAO
@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<Trip>> // Reactive Flow for live updates

    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripById(tripId: Int): Flow<Trip?> // Return Flow for composability
}

// Expense DAO
@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getExpensesByTripId(tripId: Int): Flow<List<Expense>>
}

// Participant DAO
@Dao
interface ParticipantDao {
    @Insert
    suspend fun insert(participant: Participant): Long

    @Update
    suspend fun update(participant: Participant)

    @Delete
    suspend fun delete(participant: Participant)

    @Query("SELECT * FROM participants WHERE tripId = :tripId ORDER BY name ASC")
    fun getParticipantsByTripId(tripId: Int): Flow<List<Participant>>
}
