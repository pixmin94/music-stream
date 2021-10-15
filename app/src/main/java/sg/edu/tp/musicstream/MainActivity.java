package sg.edu.tp.musicstream;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



import sg.edu.tp.musicstream.util.AppUtil;


public class MainActivity extends AppCompatActivity {

    private SongCollection songCollection  = new SongCollection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



    public void sendDataToActivity(Song song) {
        //1. Create a new Intent and specify the source and destination screen/activity.
        Intent intent = new Intent(this, PlaySongActivity.class);

        //2. Store the song information into the Intent object to be sent over to the destination screen
        intent.putExtra("id", song.getId());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("artist", song.getArtist());
        intent.putExtra("fileLink", song.getFileLink());
        intent.putExtra("coverArt", song.getCoverArt());

        //3. Launch the destination screen/activity
        startActivity(intent);

    }

    public void handleSelection(View view) {
        //1. Get the ID of the selected song.
        String resourceID = AppUtil.getResourceId(this, view);

        //2. Search for the selected song based on the ID so that all information/data of the song can be retrieved from a song list.
        Song selectedSong = songCollection.searchById(resourceID);

        //3. Popup a message on the screen to show the title of the song.
        AppUtil.popMessage(this, "Streaming song: " + selectedSong.getTitle());


        //4. Send the song data to the player screen to be played.
        sendDataToActivity(selectedSong);

    }




}