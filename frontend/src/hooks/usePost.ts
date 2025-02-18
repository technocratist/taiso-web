import { useState, useCallback } from "react";

function usePost<T>(postFunction: (...args: any[]) => Promise<T>) {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<T | null>(null);
  const [error, setError] = useState<any>(null);

  const executePost = useCallback(
    async (...args: any[]): Promise<T> => {
      setLoading(true);
      setError(null); // 새로운 요청 시작 시 에러 초기화
      try {
        const result = await postFunction(...args);
        setData(result);
        return result;
      } catch (err) {
        setError(err);
        throw err;
      } finally {
        setLoading(false);
      }
    },
    [postFunction]
  );

  return { executePost, data, loading, error };
}

export default usePost;
