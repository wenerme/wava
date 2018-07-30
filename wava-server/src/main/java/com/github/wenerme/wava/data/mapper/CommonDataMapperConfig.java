package com.github.wenerme.wava.data.mapper;

import com.github.wenerme.wava.mapper.BaseMapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;

/**
 * 如果定义了自己的基础 Mapper, 则不应该使用这个 Mapper 类,可拷贝这个配置,然后添加自己的 Mapper 到 uses 中
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 16/6/2
 */
@MapperConfig(
    uses = {BaseMapper.class, DataMapper.class, EntityMapper.class},
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@SuppressWarnings("unused")
public interface CommonDataMapperConfig {}
