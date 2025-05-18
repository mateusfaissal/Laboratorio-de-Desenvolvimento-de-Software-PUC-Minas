import React, { createContext, useCallback, useEffect, useState } from 'react';
import type { 
  AuthContextData, 
  AuthProviderProps, 
  AuthState, 
  SignInCredentials, 
  //User 
} from '../types';
import * as authService from '../services/auth';

export const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [data, setData] = useState<AuthState>({
    user: null,
    token: null,
    isAuthenticated: false,
    isLoading: true,
  });

  useEffect(() => {
    async function loadStoredData(): Promise<void> {
      const { user, token } = authService.getStoredAuthData();

      if (user && token) {
        setData({
          user,
          token,
          isAuthenticated: true,
          isLoading: false,
        });
      } else {
        setData(prevState => ({
          ...prevState,
          isLoading: false,
        }));
      }
    }

    loadStoredData();
  }, []);

  const signIn = useCallback(async (credentials: SignInCredentials) => {
    const response = await authService.signIn(credentials);
    
    authService.storeAuthData(response);
    
    setData({
      user: response.user,
      token: response.token,
      isAuthenticated: true,
      isLoading: false,
    });
  }, []);

  const signOut = useCallback(() => {
    authService.clearAuthData();
    
    setData({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
    });
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user: data.user,
        token: data.token,
        isAuthenticated: data.isAuthenticated,
        isLoading: data.isLoading,
        signIn,
        signOut,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};