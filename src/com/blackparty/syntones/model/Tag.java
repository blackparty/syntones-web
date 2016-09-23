package com.blackparty.syntones.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

@Entity
@Table(name = "tag_tbl")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "tag")
	private String tag;

	@Transient
	private List<TagSynonym> synonyms;

	public Tag() {

	}

	public List<TagSynonym> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<TagSynonym> synonyms) {
		this.synonyms = synonyms;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", tag=" + tag + "]";

	}

}
