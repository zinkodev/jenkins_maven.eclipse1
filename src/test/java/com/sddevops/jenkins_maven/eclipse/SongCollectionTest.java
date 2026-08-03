package com.sddevops.jenkins_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sddevops.jenkins_maven.eclipse.Song;
import com.sddevops.jenkins_maven.eclipse.SongCollection;

import static org.mockito.Mockito.*;

class SongCollectionTest {

	private SongCollection sc;
	private Song s1;
	private Song s2;
	private Song s3;
	private Song s4;
	private final int SONG_COLLECTION_SIZE = 4;
	private SongCollection sc_with_size;
	private SongCollection sc_with_size_1;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		sc = new SongCollection();
		s1 = new Song("001", "good 4 u", "Olivia Rodrigo", 3.59);
		s2 = new Song("002", "Peaches", "Justin Bieber", 3.18);
		s3 = new Song("003", "MONTERO", "Lil Nas", 2.3);
		s4 = new Song("004", "bad guy", "billie eilish", 3.14);
		sc.addSong(s1);
		sc.addSong(s2);
		sc.addSong(s3);
		sc.addSong(s4);
		sc_with_size = new SongCollection(5);
		sc_with_size_1 = new SongCollection(1);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		sc = null;
		sc_with_size = null;
		sc_with_size_1 = null;

	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#getSongs()}.
	 */
	@Test
	void testGetSongs() {
		List<Song> testSc = sc.getSongs();
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#addSong(com.sddevops.jenkins_maven.eclipse.Song)}.
	 */
	@Test
	void testAddSong() {
		List<Song> testSc = sc.getSongs();
		// Assert that Song Collection is equals to Song Collection Size : 4
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
		// Act
		sc.addSong(s1);
		// Assert that Song Collection is equals to Song Collection Size + 1 : 5
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE + 1);

		sc_with_size_1.addSong(s1);
		sc_with_size_1.addSong(s2);
		sc_with_size_1.addSong(s3);
		assertEquals(sc_with_size_1.getSongs().size(), 1);

	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#sortSongsByTitle()}.
	 */
	@Test
	void testSortSongsByTitle() {
		List<Song> sortedSongList = sc.sortSongsByTitle();
		assertEquals(sortedSongList.get(0).getTitle(), "MONTERO");
		assertEquals(sortedSongList.get(1).getTitle(), "Peaches");
		assertEquals(sortedSongList.get(2).getTitle(), "bad guy");
		assertEquals(sortedSongList.get(3).getTitle(), "good 4 u");

	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#sortSongsBySongLength()}.
	 */
	@Test
	void testSortSongsBySongLength() {
		List<Song> sortedSongByLengthList = sc.sortSongsBySongLength();
		assertEquals(sortedSongByLengthList.get(0).getSongLength(), 3.59);
		assertEquals(sortedSongByLengthList.get(1).getSongLength(), 3.18);
		assertEquals(sortedSongByLengthList.get(2).getSongLength(), 3.14);
		assertEquals(sortedSongByLengthList.get(3).getSongLength(), 2.3);

	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#findSongsById(java.lang.String)}.
	 */
	@Test
	void testFindSongsById() {
		Song song = sc.findSongsById("004");
		assertEquals(song.getArtiste(), "billie eilish");
		assertNull(sc.findSongsById("doesnt exist"));

	}

	/**
	 * Test method for
	 * {@link com.sddevops.jenkins_maven.eclipse.SongCollection#findSongByTitle(java.lang.String)}.
	 */
	@Test
	void testFindSongByTitle() {
		Song song = sc.findSongByTitle("MONTERO");
		assertEquals(song.getArtiste(), "Lil Nas");
		assertNull(sc.findSongByTitle("doesnt exist"));

	}
	
	@Test
	void testFetchSongOfTheDay() {
		String mockJson = """
			{
				"id": "001",
				"title": "Mock Song",
				"artiste": "Mock Artist",
				"songLength": 4.25
			}
			""";

		// Spy on a real SongCollection, but override fetchSongJson() to return our fixed JSON
		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		// Call the real fetchSongOfTheDay() — it will use our mocked fetchSongJson()
		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);
		assertEquals("001", result.getId());
		assertEquals("Mock Song", result.getTitle());
		assertEquals("Mock Artist", result.getArtiste());
		assertEquals(4.25, result.getSongLength());
	}
	
	
	@Test
	void testInvalidFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doReturn(null).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNull(result);
	}
	
	@Test
	void testExceptionHandlingInFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doThrow(new RuntimeException("API failed")).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNull(result);
		assertEquals(0, collection.getSongs().size());
	}
	
	@Test
	void testFetchSongOfTheDay_TaylorSwift() {
		String mockJson = """
			{
				"id": "010",
				"title": "Cruel Summer",
				"artiste": "Taylor Swift",
				"songLength": 2.98
			}
			""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);
		assertEquals("TS", result.getArtiste());
		assertEquals(1, collection.getSongs().size());
		assertTrue(collection.getSongs().contains(result));
	}

	@Test
	void testFetchSongOfTheDay_BrunoMars() {
		String mockJson = """
			{
				"id": "011",
				"title": "Uptown Funk",
				"artiste": "Bruno Mars",
				"songLength": 4.30
			}
			""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);
		assertEquals("BM", result.getArtiste());
		assertEquals(1, collection.getSongs().size());
		assertTrue(collection.getSongs().contains(result));
	}

	@Test
	void testFetchSongOfTheDay_OtherArtiste() {
		String mockJson = """
			{
				"id": "012",
				"title": "Some Song",
				"artiste": "Random Artist",
				"songLength": 3.20
			}
			""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);
		assertEquals("Random Artist", result.getArtiste()); // unchanged
		assertEquals(0, collection.getSongs().size());       // not added
	}
}
