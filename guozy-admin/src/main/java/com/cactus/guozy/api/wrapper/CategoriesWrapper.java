package com.cactus.guozy.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Category;

@XmlRootElement(name = "categories")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class CategoriesWrapper {

	@XmlElement(name = "category")
	private List<CategoryWrapper> categories;

	public void wrapDetails(List<Category> cats) {
		wrapSummary(cats);
	}

	public void wrapSummary(List<Category> cats) {
		for (Category category : cats) {
			CategoryWrapper wrapper = new CategoryWrapper();
			wrapper.wrapSummary(category);
			categories.add(wrapper);
		}
	}

	public List<Category> upwrap() {
		List<Category> categories = new ArrayList<Category>();
		for (CategoryWrapper c : getCategories()) {
			categories.add(c.upwrap());
		}
		return categories;
	}

	public List<CategoryWrapper> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryWrapper> categories) {
		this.categories = categories;
	}

}
