@Test
public void testValidateAndSavePriceCheck_ExceptionThrown() {
    PriceChecksForm priceChecksForm = new PriceChecksForm();
    PriceChecksService priceChecksServiceSpy = spy(PriceChecksService.class);
    List<String> errors = new ArrayList<>();
    
    doThrow(new RuntimeException("Oops")).when(priceChecksServiceSpy).attemptValidateAndSavePriceCheck(priceChecksForm, errors, appUser);
    
    Optional<PriceChecksModel> result = priceChecksServiceSpy.validateAndSavePriceCheck(priceChecksForm, errors, appUser);
    
    assertFalse(result.isPresent());
    assertEquals(1, errors.size());
    assertEquals(AuditErrors.INTERNAL_SAVE_ERROR.getValue(), errors.get(0));
}
