package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.UserService;

@Repository
@Transactional
public class PlaylistDAO {
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserService userService;

	@Autowired
	SongService songService;
	@Autowired
	PlaylistSongService playlistSongService;

	public void removePlaylist(Playlist playlist) throws Exception {
		// deleting songs on the playlist first;
		playlistSongService.removePlaylist(playlist);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("delete from Playlist where playlistId=:id");
		query.setLong("id", playlist.getPlaylistId());
		query.executeUpdate();
		session.flush();
		session.close();

	}

	public long addGeneratedPlaylist(Playlist playlist) throws Exception {
		System.out.println("SAVING GENERATED PLAYLIST TO DB.");
		User fetchedUser = userService.getUser(playlist.getUser());
		playlist.setUser(fetchedUser);
		Session session = sessionFactory.openSession();
		long playlistId = (long) session.save(playlist);
		session.flush();
		session.close();
		return playlistId;
	}

	public void addPlaylist(Playlist playlist) throws Exception {
		// complete details needed for playlist
		// fetch details for user
		User fetchedUser = userService.getUser(playlist.getUser());
		playlist.setUser(fetchedUser);
		Session session = sessionFactory.openSession();
		session.save(playlist);
		session.flush();
		session.close();
	}

	public Playlist getSongsFromPlaylist(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select song_id from playlist_song b where b.playlist_id =:id");
		query.setLong("id", id);
		List<String> songId = new ArrayList<String>();
		List<Song> songList = new ArrayList<Song>();
		for (int i = 0; i < query.list().size(); i++) {
			System.out.println(query.list().get(i));
			songList.add(songService.getSong((long) query.list().get(i)));
		}

		query = session.createSQLQuery("select playlist_name from playlist_tbl b where b.playlist_id = :id");
		query.setLong("id", id);
		Playlist fetchedPlaylist = new Playlist();
		fetchedPlaylist.setSongs(songList);
		fetchedPlaylist.setPlaylistName((String) query.uniqueResult());
		session.flush();
		session.close();

		return fetchedPlaylist;
	}

	public List<Playlist> getPlaylist(User user) throws Exception {
		// fetching the playlistId
		User fetchedUser = (User) userService.getUser(user);
		// System.out.println("getting playlist for user:
		// "+fetchedUser.getUserId());
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Playlist b where b.user.userId=:id");
		query.setLong("id", fetchedUser.getUserId());
		List<Playlist> playlists = query.list();
		session.flush();
		session.close();
		return playlists;
	}

}
