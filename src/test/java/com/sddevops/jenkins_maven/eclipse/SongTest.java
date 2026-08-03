package com.sddevops.jenkins_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sddevops.jenkins_maven.eclipse.Song;

class SongTest {
	private Song song;
	private Song song2;
	private Song song3;


	@BeforeEach
	void setUp() throws Exception {
		song = new Song("111", "Love Story", "Taylor Swift", 5.55);
	}

	@AfterEach
	void tearDown() throws Exception {
		song=null;
		song2=null;
		song3=null;

	}

	@Test
	void testHashCode() 
	{
		song = new Song("111", "Love Story", "Taylor Swift", 5.55);
		song2 = new Song("111", "Love Story", "Taylor Swift", 5.55);
		song3 = new Song("222", "Not Love Story", "Not Taylor Swift", 5.55);


		int song_1_hash = song.hashCode();
		int song_2_hash = song2.hashCode();
		int song_3_hash = song3.hashCode();

		assertEquals(song_1_hash,song_2_hash);
		assertFalse(song_1_hash == song_3_hash);

				
	}

	@Test
	void testSong() {
		song2 = new Song("222", "Addicted", "Simple Plan", 5.55);
		assertEquals(song2.getArtiste(),"Simple Plan");
	}

	@Test
	void testGetId() {
		assertEquals(song.getId(),"111");
	}

	@Test
	void testSetId() {
		song.setId("555");
		assertEquals(song.getId(),"555");
	}

	@Test
	void testGetTitle() {
		assertEquals(song.getTitle(),"Love Story");
	}

	@Test
	void testSetTitle() {
		song.setTitle("Not Love story");
		assertEquals(song.getTitle(),"Not Love story");
	}

	@Test
	void testGetArtiste() {
		assertEquals(song.getArtiste(),"Taylor Swift");
	}

	@Test
	void testSetArtiste() {
		song.setArtiste("Not Taylor");
		assertEquals(song.getArtiste(),"Not Taylor");
	}

	@Test
	void testGetSongLength() {
		assertEquals(song.getSongLength(),5.55);

	}

	@Test
	void testSetSongLength() {
		song.setSongLength(3.33);

		assertEquals(song.getSongLength(),3.33);
	}

	@Test
	void testEqualsObject() {
		Song song3 = new Song("111", "Love Story", "Taylor Swift", 5.55);
		Song song4 = new Song("222", "I'm Just A Kid", "Simple Plan", 4.44);

		assertTrue(song.equals(song));
		assertFalse(song.equals("123"));
		assertTrue(song.equals(song3));
		assertFalse(song.equals(song4));
	}
	
	@Test
	void testTitleComparator() {
		song2 = new Song("222", "Addicted", "Simple Plan", 3.0);
		assertTrue(Song.titleComparator.compare(song, song2) > 0); // "Love Story" > "Addicted"
	}

	@Test
	void testSongLengthComparator() {
		song2 = new Song("222", "Addicted", "Simple Plan", 3.00);   // shorter than song
		song3 = new Song("333", "Perfect", "Ed Sheeran", 5.55);     // same length as song

		// Case 1: s1 longer than s2 -> -1
		assertEquals(-1, Song.songLengthComparator.compare(song, song2));

		// Case 2: equal lengths -> 0
		assertEquals(0, Song.songLengthComparator.compare(song, song3));

		// Case 3: s1 shorter than s2 -> 1
		assertEquals(1, Song.songLengthComparator.compare(song2, song));
	}

	
	@Test
	void testToString() {
		assertEquals("Love Story by Taylor Swift", song.toString());
	}
}
