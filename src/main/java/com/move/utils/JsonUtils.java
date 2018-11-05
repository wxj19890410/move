package com.move.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Json帮助类
 * 
 * @author Administrator
 * 
 */
public class JsonUtils {
	private static ThreadLocal<Gson> gson = new ThreadLocal<Gson>() {
		protected synchronized Gson initialValue() {
			GsonBuilder gb = new GsonBuilder();
			gb.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
			
			gb.registerTypeAdapter(Float.class, new TypeAdapter<Float>() {
				@Override
				public Float read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					}
					
					String stringValue = reader.nextString();
					
					try {
						Float value = Float.valueOf(stringValue);
						return value;
					} catch (NumberFormatException e) {
						return null;
					}
				}

				@Override
				public void write(JsonWriter writer, Float value) throws IOException {
					if (value == null) {
						writer.nullValue();
						return;
					}
					writer.value(value);
				}
			});
			
			gb.registerTypeAdapter(Double.class, new TypeAdapter<Double>() {
				@Override
				public Double read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					}
					
					String stringValue = reader.nextString();
					
					try {
						Double value = Double.valueOf(stringValue);
						return value;
					} catch (NumberFormatException e) {
						return null;
					}
				}

				@Override
				public void write(JsonWriter writer, Double value) throws IOException {
					if (value == null) {
						writer.nullValue();
						return;
					}
					writer.value(value);
				}
			});			
			
			gb.registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
				@Override
				public Integer read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					}
					
					String stringValue = reader.nextString();
					
					try {
						Integer value = Integer.valueOf(stringValue);
						return value;
					} catch (NumberFormatException e) {
						return null;
					}
				}

				@Override
				public void write(JsonWriter writer, Integer value) throws IOException {
					if (value == null) {
						writer.nullValue();
						return;
					}
					writer.value(value);
				}
			});	
			
			gb.registerTypeAdapter(Long.class, new TypeAdapter<Long>() {
				@Override
				public Long read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					}
					
					String stringValue = reader.nextString();
					
					try {
						Long value = Long.valueOf(stringValue);
						return value;
					} catch (NumberFormatException e) {
						return null;
					}
				}

				@Override
				public void write(JsonWriter writer, Long value) throws IOException {
					if (value == null) {
						writer.nullValue();
						return;
					}
					writer.value(value);
				}
			});	
			
			gb.registerTypeAdapter(BigDecimal.class, new TypeAdapter<BigDecimal>() {
				@Override
				public BigDecimal read(JsonReader reader) throws IOException {
					if (reader.peek() == JsonToken.NULL) {
						reader.nextNull();
						return null;
					}
					
					String stringValue = reader.nextString();
					
					try {
						return new BigDecimal(stringValue);
					} catch (NumberFormatException e) {
						return null;
					}
				}

				@Override
				public void write(JsonWriter writer, BigDecimal value) throws IOException {
					if (value == null) {
						writer.nullValue();
						return;
					}
					writer.value(value);
				}
			});	
			
			
			gb.addSerializationExclusionStrategy(new ExclusionStrategy() {
				public boolean shouldSkipField(FieldAttributes fieldAttributes) {
					final Expose expose = fieldAttributes.getAnnotation(Expose.class);
					return expose != null && !expose.serialize();
				}

				public boolean shouldSkipClass(Class<?> aClass) {
					return false;
				}
			});
			gb.addDeserializationExclusionStrategy(new ExclusionStrategy() {
				public boolean shouldSkipField(FieldAttributes fieldAttributes) {
					final Expose expose = fieldAttributes.getAnnotation(Expose.class);
					return expose != null && !expose.deserialize();
				}

				public boolean shouldSkipClass(Class<?> aClass) {
					return false;
				}
			});
			gb.setDateFormat("yyyy-MM-dd HH:mm");
			return gb.create();
		}
	};

	/**
	 * json序列化
	 * 
	 * @param obj
	 *            转换的对象
	 * @return json字符串
	 */
	public static String toJson(Object obj) {
		return gson.get().toJson(obj);
	}

	/**
	 * json反序列化
	 * 
	 * @param str
	 *            json字符串
	 * @param classOfT
	 *            转换类型
	 * @return 转换的对象
	 */
	public static <T> T fromJson(String str, Class<T> classOfT) {
		return gson.get().fromJson(str, classOfT);
	}

	/**
	 * json反序列化
	 * 
	 * @param str
	 *            json字符串
	 * @param typeOfT
	 *            转换类型
	 * @return 转换的对象
	 */
	public static <T> T fromJson(String str, Type typeOfT) {
		return gson.get().fromJson(str, typeOfT);
	}

	public static <T> T fromJson(JsonElement element, Class<T> classOfT) {
		return gson.get().fromJson(element, classOfT);
	}

	public static <T> T fromJson(JsonElement element, Type typeOfT) {
		return gson.get().fromJson(element, typeOfT);
	}
	
	public static JsonElement fromJson(String str) {
		return new JsonParser().parse(str);
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(JsonElement obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		} else if (String.class.equals(clazz)) {
			return (T) ((Object) obj.getAsString());
		} else if (Long.class.equals(clazz)) {
			return (T) ((Object) obj.getAsLong());
		} else if (Integer.class.equals(clazz)) {
			return (T) ((Object) obj.getAsInt());
		}
		
		return null;
	}
}
