/* eslint-disable comma-dangle */
import React, { useState, useEffect, forwardRef } from 'react';

import './style/index.less';
import Input from 'components/input';
import Icon from 'components/icon';
import { getPrefix } from '../../lib/prefixUtil';

export default React.memo(
  forwardRef(
    (
      {
        prefix,
        placeholder,
        onChange,
        keyDown,
        onBlur,
        defaultValue,
        config,
        ...restProps
      },
      ref
    ) => {
      const currentPrefix = getPrefix(prefix);
      const [value, setValue] = useState(defaultValue);
      const finalValue = 'value' in restProps ? restProps.value : value;
      const _onChange = (e) => {
        setValue(e.target.value);
        onChange && onChange(e);
      };
      const onKeyDown = (e) => {
        keyDown && keyDown(e, finalValue);
      };
      return (
        <div className={`${currentPrefix}-search-input`} ref={ref}>
          <Icon
            className={`${currentPrefix}-search-input-icon`}
            type="icon-sousuo"
          />
          <Input
            value={finalValue}
            placeholder={placeholder}
            onChange={_onChange}
            onBlur={onBlur}
            onKeyDown={onKeyDown}
          />
        </div>
      );
    }
  )
);
