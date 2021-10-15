package sg.edu.tp.musicstream;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.IOException;

import sg.edu.tp.musicstream.util.AppUtil;

public class PlaySongActivity extends AppCompatActivity {

    // This is the constant variable that contains the website URL
    //where we will stream the music
    private static final String BASE_URL = "https://www.mboxdrive.com/";

    //these variable are the song information that we will be using in the codes here
    private String songId = "";
    private String title = "";
    private String artist = "";
    private String fileLink = "";
    private String coverArt = "";
    private String url = "";

    //this is the built in mediaplayer object that we will use to play the music
    private MediaPlayer player = null;

    //this is the position of the song in playback
    //we will set it to 0 here so that it starts in the beginning
    private int  musicPosition = 0;

    //this button variable is created to link to the play button
    //at the playback screen. we need to do this because it will act as both play and pause
    private Button btnPlayPause = null;

    //we need to create an instance of the songcollection obj so that we can get the next and previous song
    private SongCollection songCollection = new SongCollection();

    //the handler for the seekbar to keep updating the progress every second
    private Handler sbHandler = new Handler();

    //create variable for switch button for loop and shuffle
    private Switch loopBtn;
    private Switch shuffleBtn;

    //Tag for error info
    //private static final String TAG = "PlaySongActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        retrieveData();
        displaySong(title, artist, coverArt);
        SeekBar seekBar = findViewById(R.id.seekBar);
        loopBtn = findViewById(R.id.loopBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);

        //when the user clicks on the seek bar it will change the music position
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setMax(player.getDuration()/1000); //set the max of the seekbar to music duration
                if(player != null && fromUser) {
                    player.seekTo(progress * 1000); //play the music player from the position inputted
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //move seekbar based on current position
        PlaySongActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(player != null){
                    int mCurrentPosition = player.getCurrentPosition() / 1000; //get music position
                    seekBar.setProgress(mCurrentPosition); //set position
                }
                sbHandler.postDelayed(this, 1000); //keep updating the seek bar
            }
        });
    }


     //this is the function to retrieve the data from the intent.getextra
     private void retrieveData() {
        Bundle songData = this.getIntent().getExtras();
        songId = songData.getString("id");
        title = songData.getString("title");
        artist = songData.getString("artist");
        fileLink = songData.getString("fileLink");
        coverArt = songData.getString("coverArt");

         //Code to log out the value: declare where u need:
        // Log.i(TAG, "File Link " + fileLink);
        url = BASE_URL + fileLink;
    }

    private void displaySong(String title, String artist, String coverArt) {
        //This is to retrieve the song title TextView from the UI screen.
        TextView txtTitle = findViewById(R.id.txtSongTitle);

        //this is to set the text of the song title TextView to the selected title.
        txtTitle.setText(title);

        //this is to retrieve the artist TextView from the UI Screen.
        TextView txtArtiste = findViewById(R.id.txtArtist);

        //this is to set the text of the artiste TextView to the selected artist name.
        txtArtiste.setText(artist);

        // this is to get the id of the cover art from the drawble folder
        int imageID = AppUtil.getImageIdFromDrawable(this, coverArt);

        //this is to retrieve the cover art imageview from the ui screen
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);

        //this is to set the selected cover art image to the imageview in the screen
        iCoverArt.setImageResource(imageID);

    }

    private void preparePlayer() {
        //1. Create a new Media PLayer
        player = new MediaPlayer();
        //The try and catch codes are required by the prepare() method.
        //It is to catch any error that may occur and to handle the error.
        //You don't have to worry about this for now.//
        //Right here, the error is printed out to the console using the method printStackTrace().
        try{
            //2. This sets the Audio Stream Type to music streaming.
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //3. Set the source of the music.
            //For example, the url for Billie Jean will look like:
            //https://p.scdn.co/mp3-preview/4eb779428d40d579f14d12a9daf98fc66c7d0be4?
            //cid=2afe87a64b0042dabf51f37318616965
            player.setDataSource(url);

            //4.Prepare the player for playback.
            player.prepare();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void playOrPauseMusic(View view) {

        //1. If no MediaPlayer object is created, call
        //preparePlayer method to create it.
        if(player == null) {
            preparePlayer();
        }

        //2. If the player is not playing
        if(!player.isPlaying()) {
            //1. If the position of the music is greater than 0
            if(musicPosition > 0) {
                //1. Get the player to go to the music position
                player.seekTo(musicPosition);
            }

            //2. Start the player
            player.start();

            //3. Set the text of the play button to "PAUSE"
            btnPlayPause.setText("PAUSE");

            //4.Set the top bar title of the app to the music
            // that is currently playing.
            setTitle("Now Playing: " + title + " - " + artist);

            //5. When the music ends, stop the player.
            gracefullyStopsWhenMusicEnds();
        }
        else {

            //3. Pause the music
            pauseMusic();
        }
    }

    private void pauseMusic() {
        //1.Pause the player
        player.pause();

        //2. Get the current position of the music that is playing.
        musicPosition = player.getCurrentPosition();

        //3. Set the text on the button back to "Play"
        btnPlayPause.setText("PLAY");
    }

    public void gracefullyStopsWhenMusicEnds() {
        //when the player reaches the end of the music, it will check if the loop switch is checked,
        //if so, it will call the loopSong method, if not it will check if the shuffle switch is checked,
        //if so, it will call the shuffleSong method, if not it will call stopActivities method.
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (loopBtn.isChecked())
                    loopSong(null);

                else if (shuffleBtn.isChecked())
                    shuffleSong(null);

                    else
                        stopActivities();
            }
        });
    }

    private void stopActivities() {
        // If player exits, which is not null
        if(player != null) {
            // This is to set the text of the button to "PLAY"
            btnPlayPause.setText("PLAY");
            // This is to set the musicPosition variable to 0
            musicPosition = 0;
            // This is to set the setTitle to empty
            setTitle("");
        /* This is to stop and release the player. Set the player to null.
         To release the resources */
            player.stop();
            player.release();
            player = null;
        }
    }

    public void playNext(View view) {
    /*1. Create a Song variable called nextSong and assign the value from using
    using the getNextSong method that is inside the SongCollection object.
    The parameter to be pass into the getNextSong method will be the current song ID.
     */
        Song nextSong = songCollection.getNextSong(songId);

        //2. If the nextSong exist, which is not null
        if(nextSong != null) {
            // Assign all the song data to the variables: songId, title, artiste etc.
            songId = nextSong.getId();
            title = nextSong.getTitle();
            artist = nextSong.getArtist();
            fileLink = nextSong.getFileLink();
            coverArt = nextSong.getCoverArt();

            //2. Form the full URL of the song
            url = BASE_URL + fileLink;

            //3. Display the next song info on the screen using the display Song method
            displaySong(title, artist, coverArt);

            //4. Call the stopActivities method to stop the current playing song
            stopActivities();

            //5. Call the playOrPauseMusic method to play the song
            playOrPauseMusic(view);
        }
    }

    public void playPrevious(View view) {
    /* 1. Create a variable called prevSong and assign the value from using
    the getPrevSong method that is inside the SongCollection object.
    The parameter to be passed into the getPrevSong method will be the current song id.
     */
        Song prevSong = songCollection.getPrevSong(songId);

        //2. if the prevSong exist, which is not null
        if (prevSong != null){
            //1. Assign all the song data to variables; song id, title, artiste etc
            songId = prevSong.getId();
            title = prevSong.getTitle();
            artist = prevSong.getArtist();
            fileLink = prevSong.getFileLink();
            coverArt = prevSong.getCoverArt();

            //2. Form the full url of the song
            url = BASE_URL + fileLink;

            //3. Display the prevSong info on the screen using the display Song method
            displaySong(title, artist, coverArt);

            //4. called the stopActivities method to stop the current playing song
            stopActivities();

            //5. called the playOrPauseMusic method to play the song
            playOrPauseMusic(view);
        }
    }

    public void loopSong(View view) {
        //this method is called when the music ends, it will check that the player is not null first,
        //so it will replay the song that just ended
        if (player != null){
            player.start();
        }

    }

    public void shuffleSong(View view) {

        if (player != null) {
            //1. stop activities to release the resources and set player to null
            stopActivities();

            //2. create a song variable called randSong and assign value from getRandomSong method from SongCollection
            Song randSong = songCollection.getRandomSong();
            if (randSong != null){
                //1. Assign all the song data to variables; song id, title, artiste etc
                songId = randSong.getId();
                title = randSong.getTitle();
                artist = randSong.getArtist();
                fileLink = randSong.getFileLink();
                coverArt = randSong.getCoverArt();

                //2. Form the full url of the song
                url = BASE_URL + fileLink;

                //3. Display the prevSong info on the screen using the display Song method
                displaySong(title, artist, coverArt);

                //4. call playOrPauseMusic method to prepare and start player.
                playOrPauseMusic(view);
            }
        }

    }




}

