package com.qimeixun.modules.data.service;

import com.qimeixun.vo.DataChartVO;
import com.qimeixun.vo.DataVO;

import java.util.List;
import java.util.Map;

public interface DataService {

    DataVO selectData();

    Map<String, List<DataChartVO>> selectChart();
}
