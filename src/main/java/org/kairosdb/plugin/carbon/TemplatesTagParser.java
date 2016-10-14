package org.kairosdb.plugin.carbon;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Map;
import java.util.Arrays;

public class TemplatesTagParser implements TagParser
{
	public static final String TEMPLATES_LIST_PROP = "kairosdb.carbon.templatestagparser.templates";
	private static final String METRIC_TTL = "kairosdb.carbon.ttl";
	private static final String METRIC_INVALID_TTL = "kairosdb.carbon.invalidTtl";

	private String m_templates;
	private int invalidTtl = 0;
	private int ttl = 0;

	@Inject
	public TemplatesTagParser(
		@Named(TEMPLATES_LIST_PROP)String templates,
		@Named(METRIC_INVALID_TTL)int invalidTtlValue,
		@Named(METRIC_TTL)int ttlValue)
	{
		m_templates = templates;

		invalidTtl = invalidTtlValue;
		ttl = ttlValue;

		Templates.parse(m_templates);
	}

	@Override
	public CarbonMetric parseMetricName(String metricName)
	{
		CarbonMetric ret;
		Template template = Templates.lookup(metricName);

		if (template == null) {
			ret = invalidMetric(metricName, "no matching template");
		} else {
			if (!template.has_template()) { return null; }

			String targetMetric = template.buildMetricName(metricName);
			if (targetMetric == null) {
				ret = invalidMetric(metricName, "does not match metric name pattern", template);
			} else {
				ret = template.addTags(new CarbonMetric(targetMetric), metricName);
				ret.setTtl(ttl);
				if (ret == null) {
					ret = invalidMetric(metricName, "does not match tags pattern", template);
				}
			}
		}

		return ret;
	}

	private CarbonMetric invalidMetric(String metricName, String cause)
	{
		CarbonMetric ret = new CarbonMetric("invalidMetrics");
		ret.addTag("metricName", metricName);
		ret.addTag("cause", cause);
		ret.setTtl(invalidTtl);
		return ret;
	}

	private CarbonMetric invalidMetric(String metricName, String cause, Template template)
	{
		CarbonMetric ret = invalidMetric(metricName, cause);
		ret.addTag("templateFilter", template.getFilterSource());
		return ret;
	}
}
