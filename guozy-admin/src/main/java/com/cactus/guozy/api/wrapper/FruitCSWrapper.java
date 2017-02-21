package com.cactus.guozy.api.wrapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.FruitCommonSense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FruitCSWrapper {
	
	private Long id;
	
	@NotNull(message = "标题不可以为空")
	@Size(min=1, message = "标题不可以为空")
	private String title;
	
	private String picurl;
	
	@NotNull(message = "正文地址不可以为空")
	@Size(min=1, message = "正文地址不可以为空")
	private String cnturl;
	
	public FruitCommonSense unwrap() {
		FruitCommonSense ret = FruitCommonSense.builder()
				.title(title)
				.cnturl(cnturl)
				.picurl(picurl)
				.build();
		ret.setId(id);
		
		return ret;
		
	}
	
	public static FruitCSWrapper wrapSummary(FruitCommonSense fruitcs) {
		return FruitCSWrapper.builder()
				.title(fruitcs.getTitle())
				.cnturl(fruitcs.getCnturl())
				.picurl(RuntimeEnvConfigService.resolveSystemProperty("asset.url.prefix","")+fruitcs.getPicurl())
				.id(fruitcs.getId())
				.build();
	}
	
	public static FruitCSWrapper wrapDetail(FruitCommonSense fruitcs) {
		return wrapSummary(fruitcs);
	}

}
