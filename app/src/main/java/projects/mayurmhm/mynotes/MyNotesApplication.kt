package projects.mayurmhm.mynotes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // It will initialize hilt at the start
class MyNotesApplication : Application() {
}