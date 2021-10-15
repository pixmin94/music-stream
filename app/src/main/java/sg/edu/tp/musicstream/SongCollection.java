package sg.edu.tp.musicstream;

import java.util.Random;

public class SongCollection {

    private Song[] songs = new Song[4];

    public SongCollection ()
    {
        prepareSongs();
    }

    private void prepareSongs() {
        //song links expire by 9 Dec
        Song lovesickGirls = new Song("S1001",
                "Lovesick Girls",
                "BLACKPINK",
                "BLACKPINKLovesick%20Girls%20MV.mp3",
                3.22,
                "lovesick_girls");

        Song howYouLikeThat = new Song("S1002",
                "How You Like That", "BLACKPINK",
                "BLACKPINK%20-%20'How%20You%20Like%20That'%20MV.mp3",
                3.03, "how_you_like_that");

        Song notShy = new Song("S1003",
                "Not Shy", "ITZY",
                "ITZY%20Not%20Shy.mp3",
                2.59, "not_shy");

        Song psycho = new Song("S1004",
                "Psycho", "Red Velvet",
                "Red%20Velvet%20Psycho.mp3",
                3.35, "psycho");

        songs[0] = lovesickGirls;
        songs[1] = howYouLikeThat;
        songs[2] = notShy;
        songs[3] = psycho;
    }

    public Song searchById(String id){
        //1. Create a temporary variable named song which is of Song type. Set the variable to null.
        Song tempSong = null;

        //2.Starting from index 0 of the songs array to the last one, loop through every song item.
        // Increment the index by one after every loop so that the system knows how to go to the
        // next item until the last one.
        for(int index=0; index < songs.length; index++) {
            // 3. Store each song item to the song variable.
            tempSong = songs[index];
            // 4. Compare each song ID in song with the ID that we want to find. If they are equal,
            //  return this as the result.
            if(tempSong.getId().equals(id)){
                return tempSong;
            }
        }
        //If the song cannot be found in the Array of songs,
        //the null song object will be returned.
        return tempSong;
    }

    public Song getNextSong(String currentSongId) {
        Song song = null;
        for (int index = 0; index < songs.length; index++) {
            String tempSongId = songs[index].getId();
            if (tempSongId.equals(currentSongId)) {
                //check if it is last song, if next only have 2 items. in this case if index is 1
                // 1 < (2 - 1)  -> 1 is not less than 1, then it will set songs to index[0]
                //if index = 0, then 0 < (2 -1) -> 0 is less than song.length it will run song = songs[index + 1]
                if (index < songs.length - 1)
                    song = songs[index + 1];
                else
                    song = songs[0];
                break;
            }
        }
        //Code to log out the value: declare where u need:
       // Log.i(TAG, "Song object " + song);
        return song;
    }

    public Song getPrevSong(String currentSongId) {
        //1. Create a temporary Song object called song and set it to null
        Song song = null;
        /* 2. Starting from the last item of the song arrays to the first item,
        loop through evey song item. Decrement the index by one after every loop
       so that the system knows how to got ot the prev item until the first item
       */
        for (int index=(songs.length - 1); index >= 0; index--) {
        /*3. Create another temporary song variable and name it tempSongId
          and assign the ID of each song item to tempSong
        */
            String tempSongId = songs[index].getId();
            //4. Compare the song ID in tempSong with the current song ID using the equal() method
            if(tempSongId.equals(currentSongId)) {
                /*Check if the index of the array is more than 0, assign the
                previous item to song object
                 */
                if(index > 0)
                    song = songs[index-1];
                else
                    //if index is first item of array songs[0], then we assign
                    // song object to last item of the array
                    song = songs[songs.length-1];
                //break out of the if tempSongId.equals(currentSongId)
                break;
            }
        }
        //Return song object
        return song;
    }

    public Song getRandomSong() {
        //create a temporary song object
        Song song = null;

        //generate a random int from 0 to 3
        Random rand = new Random();
        int upperbound = 3;
        int index = rand.nextInt(upperbound);

        //assign the values of the random song selected
        song = songs[index];
        return song;

    }
}

