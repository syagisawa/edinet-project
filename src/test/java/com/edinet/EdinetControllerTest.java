package com.edinet;

import static org.junit.jupiter.api.Assertions.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.edinet.domain.services.handler.EdinetAPIHandler;
import com.edinet.jacson.Result;

public class EdinetControllerTest {

	@Mock
	URL mockUrl;
	@Mock
	HttpURLConnection mockCon;

	private AutoCloseable closeable;

	@BeforeEach
    public void init() {
		closeable = MockitoAnnotations.openMocks(this);
    }

	@Test
	public void test() throws Exception {
		EdinetAPIHandler edinetApiHandler = new EdinetAPIHandler();
		Mockito.when(mockUrl.openConnection()).thenReturn(mockCon);
		Mockito.when(mockCon.getResponseCode()).thenReturn(null);
		List<Result> docIdList = edinetApiHandler.getDocIdList("2020-12-09");
		assertEquals(docIdList, null);
	}
}
