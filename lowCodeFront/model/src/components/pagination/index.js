import React, { useState, useEffect } from 'react';
import RcPagination from 'rc-pagination';
import './style/index.less';
import 'rc-pagination/assets/index.css';
import { getPrefix } from '../../lib/prefixUtil';

const Pagination = React.memo(({ prefix, totalItems, pageSize, pageNo, cb }) => {
  const currentPrefix = getPrefix(prefix);
  // const [pageCount, setPageCount] = useState(1);
  const onChange = (currentPage) => {
    // setPageCount(currentPage)
    cb && cb(currentPage, pageSize);
  };
  return (
    <div className={`${currentPrefix}-pagination-container`}>
      <RcPagination
        hideOnSinglePage
        showQuickJumper
        current={pageNo}
        total={totalItems}
        onChange={onChange}
        pageSize={pageSize}
      />
    </div>
  );
});

export default Pagination;
