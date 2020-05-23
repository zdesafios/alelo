package br.com.test.vr.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.com.test.vr.model.SaleStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter @Setter
public class Sale implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false)
	private Long businessId;

	@Column(nullable = false)
	private Double price;
	
	@Column(nullable = false)
	private LocalDateTime date;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> tags;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SaleStatus status;
	
	public Sale() {
		tags = new ArrayList<>();
	}

	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public List<String> getList() {
		return Collections.unmodifiableList(tags);
	}
	
	@PrePersist
	private void prePersist() {
		date = LocalDateTime.now();
		status = SaleStatus.EFFECTIVE;
	}

}
