package at.ac.fhstp.learningandroiddev

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhstp.learningandroiddev.data.Trip
import at.ac.fhstp.learningandroiddev.data.TripDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripDao: TripDao
) : ViewModel() {

    // Function to insert a new trip into the database
    fun insertTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.insert(trip)
        }
    }

    // Function to get all trips as a Flow
    fun getAllTrips(): Flow<List<Trip>> {
        return tripDao.getAllTrips()
    }

    // Function to get a trip by its ID as a Flow
    fun getTripById(tripId: Int): Flow<Trip?> {
        return tripDao.getTripById(tripId)
    }
}
