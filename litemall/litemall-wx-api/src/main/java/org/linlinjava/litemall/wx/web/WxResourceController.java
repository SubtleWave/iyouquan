package org.linlinjava.litemall.wx.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.util.SpringContextUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.dao.LitemallTypefaceConfigMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 资源服务
 */
@RestController
@RequestMapping("/wx/resource")
@Validated
public class WxResourceController {
	private final Log logger = LogFactory.getLog(WxResourceController.class);

	@Autowired
	private LitemallResourceService resourceService;

	@Autowired
	private LitemallGoodsProductService productService;

	@Autowired
	private LitemallIssueService goodsIssueService;

	@Autowired
	private LitemallGoodsAttributeService goodsAttributeService;

	@Autowired
	private LitemallBrandService brandService;

	@Autowired
	private LitemallCommentService commentService;

	@Autowired
	private LitemallUserService userService;

	@Autowired
	private LitemallCollectService collectService;

	@Autowired
	private LitemallFootprintService footprintService;

	@Autowired
	private LitemallCategoryService categoryService;

	@Autowired
	private LitemallSearchHistoryService searchHistoryService;

	@Autowired
	private LitemallGoodsSpecificationService goodsSpecificationService;

	@Autowired
	private LitemallGrouponRulesService rulesService;

	private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);

	private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

	private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(16, 16, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);

	/**
	 * 商品详情
	 * <p>
	 * 用户可以不登录。
	 * 如果用户登录，则记录用户足迹以及返回用户收藏信息。
	 *
	 * @param userId 用户ID
	 * @param id     商品ID
	 * @return 商品详情
	 */
	//todo 商品详情是否需要放入缓存服务器？
	@GetMapping("detail")
	public Object detail( @NotNull Integer id) {
		LitemallTypefaceConfig info = resourceService.findById(id);
		Map<String, Object> data = new HashMap<>();
		try {
			data.put("info", info);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtil.ok(data);
	}

	/**
	 * @param page       分页页数
	 * @param limit       分页大小
	 * @param sort       排序方式，支持"add_time", "retail_price"或"name"
	 * @param order      排序类型，顺序或者降序
	 * @return 根据条件搜素的商品详情
	 */
	@GetMapping("list")
	public Object list(
		String type,
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "10") Integer limit,
		@Sort(accepts = {"add_time", "retail_price", "name"}) @RequestParam(defaultValue = "add_time") String sort,
		@Order @RequestParam(defaultValue = "desc") String order) throws IOException, SolrServerException {

		//查询列表数据

		List<LitemallTypefaceConfig> typefaceConfigs = resourceService.querySelective(type, page, limit, sort, order);


		PageInfo<LitemallTypefaceConfig> pagedList = PageInfo.of(typefaceConfigs);

		Map<String, Object> entity = new HashMap<>();
		entity.put("list", typefaceConfigs);
		entity.put("total", pagedList.getTotal());
		entity.put("page", pagedList.getPageNum());
		entity.put("limit", pagedList.getPageSize());
		entity.put("pages", pagedList.getPages());

		return ResponseUtil.ok(entity);
	}
	@GetMapping("count")
	public Object count() {
		Integer goodsCount = resourceService.queryOnSale();
		return ResponseUtil.ok(goodsCount);
	}
	@GetMapping("getfont")
	public Object getFontByName(@RequestParam String name){
		if (StringUtils.isBlank(name)){
			return ResponseUtil.badArgument();
		}
		LitemallTypefaceConfig typefaceConfig = resourceService.getFontByName(name);
		Map<String, Object> data = new HashMap<>();
		data.put("info", typefaceConfig);
		return ResponseUtil.ok(data);
	}
}