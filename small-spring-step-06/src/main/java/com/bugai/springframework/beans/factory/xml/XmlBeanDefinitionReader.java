package com.bugai.springframework.beans.factory.xml;

import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.PropertyValue;
import com.bugai.springframework.beans.factory.config.BeanDefinition;
import com.bugai.springframework.beans.factory.config.BeanReference;
import com.bugai.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.bugai.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.bugai.springframework.core.io.Resource;
import com.bugai.springframework.core.io.ResourceLoader;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


  public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
    super(registry);
  }

  public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
    super(registry, resourceLoader);
  }

  @Override
  public void loadBeanDefinitions(Resource resource) throws BeansException {
    try {
      try (InputStream inputStream = resource.getInputStream()) {
        doLoadBeanDefinitions(inputStream);
      }
    } catch (IOException | ParserConfigurationException | SAXException | ClassNotFoundException e) {
      throw new BeansException("IOException parsing XML document from " + resource, e);
    }
  }

  @Override
  public void loadBeanDefinitions(Resource... resources) throws BeansException {
    for (Resource resource : resources) {
      loadBeanDefinitions(resource);
    }
  }

  @Override
  public void loadBeanDefinitions(String locations) throws BeansException {
    ResourceLoader resourceLoader = getResourceLoader();
    Resource resource = resourceLoader.getResource(locations);
    loadBeanDefinitions(resource);
  }

  @Override
  public void loadBeanDefinitions(String... locations) throws BeansException {
    for (String location : locations) {
      loadBeanDefinitions(location);
    }
  }

  protected void doLoadBeanDefinitions(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
    Element root = doc.getDocumentElement();
    NodeList childNodes = root.getChildNodes();

    for (int i = 0; i < childNodes.getLength(); i++) {

      // 不是元素
      if (!(childNodes.item(i) instanceof Element))
        continue;

      // 不是bean对象
      if (!"bean".equals(childNodes.item(i).getNodeName()))
        continue;

      // 解析标签
      Element bean = (Element) childNodes.item(i);
      String id = bean.getAttribute("id");
      String name = bean.getAttribute("name");
      String className = bean.getAttribute("class");

      Class<?> clazz = Class.forName(className);

      String beanName = StringUtils.isNotBlank(id) ? id : name;
      if (StringUtils.isBlank(beanName)) {
        beanName = StringUtils.lowerCase(clazz.getSimpleName(), Locale.ROOT);
      }

      BeanDefinition beanDefinition = new BeanDefinition(clazz);
      for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
        if (!(bean.getChildNodes().item(j) instanceof Element))
          continue;

        if (!"property".equals(bean.getChildNodes().item(j).getNodeName()))
          continue;

        // 解析标签：property
        Element property = (Element) bean.getChildNodes().item(j);
        String attrName = property.getAttribute("name");
        String attrValue = property.getAttribute("value");
        String attrRef = property.getAttribute("ref");

        Object value = StringUtils.isNotBlank(attrRef) ? new BeanReference(attrRef) : attrValue;

        PropertyValue propertyValue = new PropertyValue(attrName, value);
        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
      }

      if (getRegister().containsBeanDefinition(beanName)) {
        throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
      }
      getRegister().registerBeanDefinition(beanName, beanDefinition);
    }
  }
}
