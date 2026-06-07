package com.example.movielist;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Movie> __insertionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __deletionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __updateAdapterOfMovie;

  public MovieDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovie = new EntityInsertionAdapter<Movie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `movies` (`id`,`title`,`posterUrl`,`year`,`director`,`casts`,`watched`,`watchedAt`,`detailsFetched`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Movie entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getPosterUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPosterUrl());
        }
        if (entity.getYear() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getYear());
        }
        if (entity.getDirector() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDirector());
        }
        if (entity.getCasts() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCasts());
        }
        final int _tmp = entity.getWatched() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getWatchedAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getWatchedAt());
        }
        final int _tmp_1 = entity.getDetailsFetched() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
      }
    };
    this.__deletionAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `movies` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Movie entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `movies` SET `id` = ?,`title` = ?,`posterUrl` = ?,`year` = ?,`director` = ?,`casts` = ?,`watched` = ?,`watchedAt` = ?,`detailsFetched` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Movie entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getPosterUrl() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPosterUrl());
        }
        if (entity.getYear() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getYear());
        }
        if (entity.getDirector() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDirector());
        }
        if (entity.getCasts() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCasts());
        }
        final int _tmp = entity.getWatched() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getWatchedAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getWatchedAt());
        }
        final int _tmp_1 = entity.getDetailsFetched() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Movie movie, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMovie.insertAndReturnId(movie);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Movie movie, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMovie.handle(movie);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Movie movie, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMovie.handle(movie);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super Movie> $completion) {
    final String _sql = "SELECT * FROM movies WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Movie>() {
      @Override
      @Nullable
      public Movie call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
          final int _cursorIndexOfCasts = CursorUtil.getColumnIndexOrThrow(_cursor, "casts");
          final int _cursorIndexOfWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "watched");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfDetailsFetched = CursorUtil.getColumnIndexOrThrow(_cursor, "detailsFetched");
          final Movie _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPosterUrl;
            if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
              _tmpPosterUrl = null;
            } else {
              _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final String _tmpDirector;
            if (_cursor.isNull(_cursorIndexOfDirector)) {
              _tmpDirector = null;
            } else {
              _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
            }
            final String _tmpCasts;
            if (_cursor.isNull(_cursorIndexOfCasts)) {
              _tmpCasts = null;
            } else {
              _tmpCasts = _cursor.getString(_cursorIndexOfCasts);
            }
            final boolean _tmpWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatched);
            _tmpWatched = _tmp != 0;
            final Long _tmpWatchedAt;
            if (_cursor.isNull(_cursorIndexOfWatchedAt)) {
              _tmpWatchedAt = null;
            } else {
              _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            }
            final boolean _tmpDetailsFetched;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDetailsFetched);
            _tmpDetailsFetched = _tmp_1 != 0;
            _result = new Movie(_tmpId,_tmpTitle,_tmpPosterUrl,_tmpYear,_tmpDirector,_tmpCasts,_tmpWatched,_tmpWatchedAt,_tmpDetailsFetched);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPending(final Continuation<? super List<Movie>> $completion) {
    final String _sql = "SELECT * FROM movies WHERE detailsFetched = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Movie>>() {
      @Override
      @NonNull
      public List<Movie> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
          final int _cursorIndexOfCasts = CursorUtil.getColumnIndexOrThrow(_cursor, "casts");
          final int _cursorIndexOfWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "watched");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfDetailsFetched = CursorUtil.getColumnIndexOrThrow(_cursor, "detailsFetched");
          final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Movie _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPosterUrl;
            if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
              _tmpPosterUrl = null;
            } else {
              _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final String _tmpDirector;
            if (_cursor.isNull(_cursorIndexOfDirector)) {
              _tmpDirector = null;
            } else {
              _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
            }
            final String _tmpCasts;
            if (_cursor.isNull(_cursorIndexOfCasts)) {
              _tmpCasts = null;
            } else {
              _tmpCasts = _cursor.getString(_cursorIndexOfCasts);
            }
            final boolean _tmpWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatched);
            _tmpWatched = _tmp != 0;
            final Long _tmpWatchedAt;
            if (_cursor.isNull(_cursorIndexOfWatchedAt)) {
              _tmpWatchedAt = null;
            } else {
              _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            }
            final boolean _tmpDetailsFetched;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDetailsFetched);
            _tmpDetailsFetched = _tmp_1 != 0;
            _item = new Movie(_tmpId,_tmpTitle,_tmpPosterUrl,_tmpYear,_tmpDirector,_tmpCasts,_tmpWatched,_tmpWatchedAt,_tmpDetailsFetched);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Movie>> getToWatch() {
    final String _sql = "SELECT * FROM movies WHERE watched = 0 ORDER BY title COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"movies"}, new Callable<List<Movie>>() {
      @Override
      @NonNull
      public List<Movie> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
          final int _cursorIndexOfCasts = CursorUtil.getColumnIndexOrThrow(_cursor, "casts");
          final int _cursorIndexOfWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "watched");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfDetailsFetched = CursorUtil.getColumnIndexOrThrow(_cursor, "detailsFetched");
          final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Movie _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPosterUrl;
            if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
              _tmpPosterUrl = null;
            } else {
              _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final String _tmpDirector;
            if (_cursor.isNull(_cursorIndexOfDirector)) {
              _tmpDirector = null;
            } else {
              _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
            }
            final String _tmpCasts;
            if (_cursor.isNull(_cursorIndexOfCasts)) {
              _tmpCasts = null;
            } else {
              _tmpCasts = _cursor.getString(_cursorIndexOfCasts);
            }
            final boolean _tmpWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatched);
            _tmpWatched = _tmp != 0;
            final Long _tmpWatchedAt;
            if (_cursor.isNull(_cursorIndexOfWatchedAt)) {
              _tmpWatchedAt = null;
            } else {
              _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            }
            final boolean _tmpDetailsFetched;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDetailsFetched);
            _tmpDetailsFetched = _tmp_1 != 0;
            _item = new Movie(_tmpId,_tmpTitle,_tmpPosterUrl,_tmpYear,_tmpDirector,_tmpCasts,_tmpWatched,_tmpWatchedAt,_tmpDetailsFetched);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Movie>> getWatched() {
    final String _sql = "SELECT * FROM movies WHERE watched = 1 ORDER BY title COLLATE NOCASE ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"movies"}, new Callable<List<Movie>>() {
      @Override
      @NonNull
      public List<Movie> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPosterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "posterUrl");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDirector = CursorUtil.getColumnIndexOrThrow(_cursor, "director");
          final int _cursorIndexOfCasts = CursorUtil.getColumnIndexOrThrow(_cursor, "casts");
          final int _cursorIndexOfWatched = CursorUtil.getColumnIndexOrThrow(_cursor, "watched");
          final int _cursorIndexOfWatchedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "watchedAt");
          final int _cursorIndexOfDetailsFetched = CursorUtil.getColumnIndexOrThrow(_cursor, "detailsFetched");
          final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Movie _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpPosterUrl;
            if (_cursor.isNull(_cursorIndexOfPosterUrl)) {
              _tmpPosterUrl = null;
            } else {
              _tmpPosterUrl = _cursor.getString(_cursorIndexOfPosterUrl);
            }
            final Integer _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            }
            final String _tmpDirector;
            if (_cursor.isNull(_cursorIndexOfDirector)) {
              _tmpDirector = null;
            } else {
              _tmpDirector = _cursor.getString(_cursorIndexOfDirector);
            }
            final String _tmpCasts;
            if (_cursor.isNull(_cursorIndexOfCasts)) {
              _tmpCasts = null;
            } else {
              _tmpCasts = _cursor.getString(_cursorIndexOfCasts);
            }
            final boolean _tmpWatched;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWatched);
            _tmpWatched = _tmp != 0;
            final Long _tmpWatchedAt;
            if (_cursor.isNull(_cursorIndexOfWatchedAt)) {
              _tmpWatchedAt = null;
            } else {
              _tmpWatchedAt = _cursor.getLong(_cursorIndexOfWatchedAt);
            }
            final boolean _tmpDetailsFetched;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfDetailsFetched);
            _tmpDetailsFetched = _tmp_1 != 0;
            _item = new Movie(_tmpId,_tmpTitle,_tmpPosterUrl,_tmpYear,_tmpDirector,_tmpCasts,_tmpWatched,_tmpWatchedAt,_tmpDetailsFetched);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
