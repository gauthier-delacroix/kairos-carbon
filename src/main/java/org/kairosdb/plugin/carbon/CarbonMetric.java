package org.kairosdb.plugin.carbon;

import com.google.common.collect.ImmutableSortedMap;
import org.kairosdb.util.Tags;

/**
 Created with IntelliJ IDEA.
 User: bhawkins
 Date: 12/10/13
 Time: 12:23 PM
 To change this template use File | Settings | File Templates.
 */
public class CarbonMetric
{
	private String m_name;
	private ImmutableSortedMap.Builder<String, String> m_tags;
	private int m_ttl = 0;

	public CarbonMetric(String name)
	{
		m_name = name;
		m_tags = Tags.create();
	}

	public void addTag(String tag, String value)
	{
		m_tags.put(tag, value);
	}

	public void setTtl(int ttl)
	{
		m_ttl = ttl;
	}

	public String getName()
	{
		return m_name;
	}

	public ImmutableSortedMap<String, String> getTags()
	{
		return m_tags.build();
	}

	public int getTtl()
	{
		return m_ttl;
	}


}