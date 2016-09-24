package com.blackparty.syntones.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "song_line_tbl")
public class SongLine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "line_number")
	private int lineNumber;
	@Column(name = "line")
	private String line;
	@Column(name = "result")
	private float result;
	@Column(name ="song_id")
	private long songId;

	public SongLine() {
	}

	public SongLine(int lineNumber, String line, float result) {
		this.lineNumber = lineNumber;
		this.line = line;
		this.result = result;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public float getResult() {
		return result;
	}

	public void setResult(float result) {
		this.result = result;
	}


	
	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	@Override
	public String toString() {
		return "SongLine [lineNumber=" + lineNumber + ", line=" + line + ", result=" + result + ", songId=" + songId
				+ "]";
	}
	
	
	public float compareTo(SongLine compareSongLine) {
        float compareResult = ((SongLine) compareSongLine).getResult();
        return compareResult - this.result;
    }

    public static Comparator<SongLine> SongLineComparator = new Comparator<SongLine>() {
        public int compare(SongLine l1, SongLine l2) {
            float line1 = l1.getResult();
            float line2 = l2.getResult();

            if (line1 > line2) {
                return -1;
            }
            if (line1 < line2) {
                return 1;
            }
            return 0;

        }
    };
	

	
}
