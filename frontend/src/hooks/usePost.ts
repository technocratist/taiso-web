// hooks/usePost.ts
import { useState } from "react";

function usePost<T>(postFunction: (...args: any[]) => Promise<T>) {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<T | null>(null);
  const [error, setError] = useState<any>(null);

  const executePost = async (...args: any[]) => {
    setLoading(true);
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
  };

  return { executePost, data, loading, error };
}

export default usePost;
