package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record MaercheneinteilungFields(
    Gruppe gruppe,
    String rolle1,
    String bilderRolle1,
    String rolle2,
    String bilderRolle2,
    String rolle3,
    String bilderRolle3,
    boolean kuchenVorstellung1,
    boolean kuchenVorstellung2,
    boolean kuchenVorstellung3,
    boolean kuchenVorstellung4,
    boolean kuchenVorstellung5,
    boolean kuchenVorstellung6,
    boolean kuchenVorstellung7,
    boolean kuchenVorstellung8,
    boolean kuchenVorstellung9,
    String zusatzattribut,
    String bemerkungen) {

  public static MaercheneinteilungFields of(Maercheneinteilung entity) {
    if (entity == null) return null;

    return new MaercheneinteilungFields(
        entity.getGruppe(),
        entity.getRolle1(),
        entity.getBilderRolle1(),
        entity.getRolle2(),
        entity.getBilderRolle2(),
        entity.getRolle3(),
        entity.getBilderRolle3(),
        entity.isKuchenVorstellung1(),
        entity.isKuchenVorstellung2(),
        entity.isKuchenVorstellung3(),
        entity.isKuchenVorstellung4(),
        entity.isKuchenVorstellung5(),
        entity.isKuchenVorstellung6(),
        entity.isKuchenVorstellung7(),
        entity.isKuchenVorstellung8(),
        entity.isKuchenVorstellung9(),
        entity.getZusatzattribut(),
        entity.getBemerkungen());
  }

  public void mergeIntoEntity(Maercheneinteilung entity) {
    if (entity == null) return;

    entity.setGruppe(gruppe());
    entity.setRolle1(rolle1());
    entity.setBilderRolle1(bilderRolle1());
    entity.setRolle2(rolle2());
    entity.setBilderRolle2(bilderRolle2());
    entity.setRolle3(rolle3());
    entity.setBilderRolle3(bilderRolle3());
    entity.setKuchenVorstellung1(kuchenVorstellung1());
    entity.setKuchenVorstellung2(kuchenVorstellung2());
    entity.setKuchenVorstellung3(kuchenVorstellung3());
    entity.setKuchenVorstellung4(kuchenVorstellung4());
    entity.setKuchenVorstellung5(kuchenVorstellung5());
    entity.setKuchenVorstellung6(kuchenVorstellung6());
    entity.setKuchenVorstellung7(kuchenVorstellung7());
    entity.setKuchenVorstellung8(kuchenVorstellung8());
    entity.setKuchenVorstellung9(kuchenVorstellung9());
    entity.setZusatzattribut(zusatzattribut());
    entity.setBemerkungen(bemerkungen());
  }
}
